package com.users.list.ui

import com.users.list.model.domain.UserEntity

interface ListContract {
  interface View {
    fun displayUserList(users: List<UserEntity>)
    fun toggleRefreshing(isRefreshing: Boolean)
    fun displayError(errorMessage: String)
  }

  interface Presenter {
    fun fetchUsers()
    fun filterUsers(searchQuery: String?)
    fun releaseResources()
  }
}