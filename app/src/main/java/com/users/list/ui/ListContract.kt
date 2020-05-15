package com.users.list.ui

import com.users.list.model.domain.UserEntity

interface ListContract {
  interface View {
    fun displayUserList(users: List<UserEntity>)
    fun updateUserListItem(userName: String, repositories: List<String>)
  }

  interface Presenter {
    fun fetchUsers()
    fun fetchUsersRepositories(userName: String)
    fun releaseResources()
  }
}