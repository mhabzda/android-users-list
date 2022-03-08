package com.users.list.ui

import com.users.list.model.domain.UserEntity

interface ListContract {
    interface View {
        fun displayUsersList(users: List<UserEntity>)
        fun toggleRefreshing(isRefreshing: Boolean)
        fun clearSearch()
        fun displayError(errorMessage: String)
    }

    interface Presenter {
        fun onCreate(view: View)
        fun onRefresh()
        fun onSearchTextChange(searchQuery: String)
        fun onClear()
    }
}
