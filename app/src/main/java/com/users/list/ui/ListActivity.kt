package com.users.list.ui

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import com.users.R
import com.users.databinding.ActivityListBinding
import com.users.list.model.domain.UserEntity
import com.users.list.ui.adapter.UsersAdapter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ListActivity : DaggerAppCompatActivity(), ListContract.View {

    @Inject
    lateinit var presenter: ListContract.Presenter

    private lateinit var binding: ActivityListBinding
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeList()
        binding.listSwipeRefresh.setOnRefreshListener { presenter.onRefresh() }

        presenter.onCreate()
    }

    private fun initializeList() = with(binding) {
        usersAdapter = UsersAdapter()
        listUsers.adapter = usersAdapter
        listUsers.addItemDecoration(DividerItemDecoration(this@ListActivity, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(createOnQueryTextListener())

        return super.onCreateOptionsMenu(menu)
    }

    override fun displayUsersList(users: List<UserEntity>) {
        usersAdapter.users = users
    }

    override fun toggleRefreshing(isRefreshing: Boolean) {
        binding.listSwipeRefresh.isRefreshing = isRefreshing
    }

    override fun clearSearch() {
        searchView.setQuery("", false)
    }

    override fun displayError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        presenter.onClear()
        super.onDestroy()
    }

    private fun createOnQueryTextListener(): SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.onSearchTextChange(newText)
                return false
            }
        }
}
