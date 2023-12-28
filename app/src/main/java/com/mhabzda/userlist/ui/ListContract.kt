package com.mhabzda.userlist.ui

import com.mhabzda.userlist.model.domain.UserEntity

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
