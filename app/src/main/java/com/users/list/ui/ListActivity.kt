package com.users.list.ui

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.users.R
import com.users.list.model.domain.UserEntity
import com.users.list.ui.adapter.UsersAdapter
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_list.swipe_refresh as swipeRefresh
import kotlinx.android.synthetic.main.activity_list.users_list as usersRecyclerView

class ListActivity : DaggerAppCompatActivity(), ListContract.View {

    @Inject
    lateinit var presenter: ListContract.Presenter

    private lateinit var usersAdapter: UsersAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initializeRecyclerView()
        swipeRefresh.setOnRefreshListener {
            presenter.fetchUsers()
            searchView.setQuery("", false)
        }

        presenter.fetchUsers()
    }

    private fun initializeRecyclerView() {
        usersAdapter = UsersAdapter()
        usersRecyclerView.adapter = usersAdapter
        usersRecyclerView.layoutManager = LinearLayoutManager(this)
        usersRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onDestroy() {
        presenter.releaseResources()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(createOnQueryTextListener())

        return super.onCreateOptionsMenu(menu)
    }

    override fun displayUserList(users: List<UserEntity>) {
        usersAdapter.users = users
    }

    override fun toggleRefreshing(isRefreshing: Boolean) {
        swipeRefresh.isRefreshing = isRefreshing
    }

    override fun displayError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun createOnQueryTextListener(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.filterUsers(newText)
                return false
            }
        }
    }
}
