package com.mhabzda.userlist.ui

import com.mhabzda.userlist.domain.model.UserEntity

interface ListContract {

    data class ListViewState(
        val users: List<UserEntity> = emptyList(),
        val isRefreshing: Boolean = false,
    )

    sealed class ListEffect {
        data object ClearSearch : ListEffect()
        data class DisplayError(val errorMessage: String) : ListEffect()
    }
}
