package com.users.list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.users.R
import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.database.LocalUserRepository
import com.users.list.model.domain.CompositeUserRepository
import com.users.list.model.domain.UserEntity
import com.users.list.ui.adapter.UsersAdapter
import com.users.list.ui.schedulers.AndroidSchedulerProvider
import kotlinx.android.synthetic.main.activity_main.user_search as userSearch
import kotlinx.android.synthetic.main.activity_main.users_list as usersRecyclerView

class ListActivity : AppCompatActivity(), ListContract.View {
  private lateinit var usersAdapter: UsersAdapter
  private lateinit var presenter: ListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val userRepository = CompositeUserRepository(LocalUserRepository(application), RemoteUserRepository())
    val schedulerProvider = AndroidSchedulerProvider()

    presenter = ListPresenter(userRepository, schedulerProvider, this)

    usersAdapter = UsersAdapter()
    usersRecyclerView.adapter = usersAdapter
    usersRecyclerView.layoutManager = LinearLayoutManager(this)
    userSearch.setOnQueryTextListener(createOnQueryTextListener())

    presenter.fetchUsers()
  }

  override fun onDestroy() {
    presenter.releaseResources()
    super.onDestroy()
  }

  override fun displayUserList(users: List<UserEntity>) {
    usersAdapter.users = users
  }

  private fun createOnQueryTextListener(): SearchView.OnQueryTextListener {
    return object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        presenter.filterUsers(newText)
        return false
      }
    }
  }
}
