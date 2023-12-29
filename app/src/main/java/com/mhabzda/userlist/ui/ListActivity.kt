package com.mhabzda.userlist.ui

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.mhabzda.userlist.R
import com.mhabzda.userlist.databinding.ActivityListBinding
import com.mhabzda.userlist.ui.ListContract.ListEffect.ClearSearch
import com.mhabzda.userlist.ui.ListContract.ListEffect.DisplayError
import com.mhabzda.userlist.ui.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListActivity : AppCompatActivity() {

    private val viewModel: ListViewModel by viewModels<ListViewModel>()

    private lateinit var binding: ActivityListBinding
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeList()
        binding.listSwipeRefresh.setOnRefreshListener { viewModel.onRefresh() }
    }

    private fun initializeList() = with(binding) {
        usersAdapter = UsersAdapter()
        listUsers.adapter = usersAdapter
        listUsers.addItemDecoration(DividerItemDecoration(this@ListActivity, DividerItemDecoration.VERTICAL))
    }

    override fun onStart() {
        super.onStart()

        viewModel.state.observeWhenStarted {
            usersAdapter.users = it.users
            binding.listSwipeRefresh.isRefreshing = it.isRefreshing
        }
        viewModel.effects.observeWhenStarted {
            when (it) {
                ClearSearch -> searchView.setQuery("", false)
                is DisplayError -> Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(createOnQueryTextListener())

        return super.onCreateOptionsMenu(menu)
    }

    private fun createOnQueryTextListener(): SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.onSearchTextChange(newText)
                return false
            }
        }

    private inline fun <reified T> Flow<T>.observeWhenStarted(
        noinline action: suspend (T) -> Unit,
    ): Job = lifecycleScope.launch {
        flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collectLatest(action)
    }
}
