package com.users.list.ui

import com.users.list.model.domain.UserEntity

interface ListContract {
  interface View {
    fun displayUserList(users: List<UserEntity>)
  }

  interface Presenter {
    fun fetchUsers()
    fun filterUsers(searchQuery: String?)
    fun releaseResources()
  }
}