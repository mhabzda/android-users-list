package com.users.list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.users.R
import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.domain.UserEntity
import com.users.list.ui.schedulers.AndroidSchedulerProvider
import kotlinx.android.synthetic.main.activity_main.users_list as usersRecyclerView

class ListActivity : AppCompatActivity(), ListContract.View {
  private lateinit var usersAdapter: UsersAdapter
  private lateinit var presenter: ListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val userRepository = RemoteUserRepository()
    val schedulerProvider = AndroidSchedulerProvider()

    presenter = ListPresenter(userRepository, schedulerProvider, this)

    usersAdapter = UsersAdapter { presenter.fetchUsersRepositories(it) }
    usersRecyclerView.adapter = usersAdapter
    usersRecyclerView.layoutManager = LinearLayoutManager(this)
  }

  override fun onResume() {
    super.onResume()

    presenter.fetchUsers()
  }

  override fun displayUserList(users: List<UserEntity>) {
    usersAdapter.updateUsersList(users)
  }

  override fun updateUserListItem(userName: String, repositories: List<String>) {
    usersAdapter.updateUser(userName, repositories)
  }
}
