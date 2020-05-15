package com.users.list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.users.R
import com.users.list.model.api.fake.FakeUserRepository
import com.users.list.model.database.LocalUserRepository
import com.users.list.model.domain.CompositeUserRepository
import com.users.list.ui.displayable.UserDisplayable
import com.users.list.ui.schedulers.AndroidSchedulerProvider
import kotlinx.android.synthetic.main.activity_main.users_list as usersRecyclerView

class ListActivity : AppCompatActivity(), ListContract.View {
  private lateinit var usersAdapter: UsersAdapter
  private lateinit var presenter: ListPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val userRepository = CompositeUserRepository(LocalUserRepository(application), FakeUserRepository())
    val schedulerProvider = AndroidSchedulerProvider()

    presenter = ListPresenter(userRepository, schedulerProvider, this)

    usersAdapter = UsersAdapter { presenter.fetchUsersRepositories(it) }
    usersRecyclerView.adapter = usersAdapter
    usersRecyclerView.layoutManager = LinearLayoutManager(this)

    presenter.fetchUsers()
  }

  override fun onDestroy() {
    presenter.releaseResources()
    super.onDestroy()
  }

  override fun displayUserList(users: List<UserDisplayable>) {
    usersAdapter.updateUsersList(users)
  }

  override fun updateUserListItem(userName: String, repositories: List<String>) {
    usersAdapter.updateUser(userName, repositories)
  }
}
