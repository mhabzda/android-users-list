package com.mhabzda.userlist.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mhabzda.userlist.base.BaseViewModel
import com.mhabzda.userlist.domain.RetrieveUsersUseCase
import com.mhabzda.userlist.domain.model.UserEntity
import com.mhabzda.userlist.ui.ListContract.ListEffect
import com.mhabzda.userlist.ui.ListContract.ListEffect.DisplayError
import com.mhabzda.userlist.ui.ListContract.ListViewState
import com.mhabzda.userlist.ui.filter.ListItemsFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val retrieveUsersUseCase: RetrieveUsersUseCase,
    private val listItemsFilter: ListItemsFilter,
) : BaseViewModel<ListViewState, ListEffect>(
    defaultState = ListViewState(),
) {

    private val usersListCache: MutableList<UserEntity> = mutableListOf()

    init {
        fetchUsers()
    }

    fun onRefresh() {
        fetchUsers()
        emit(ListEffect.ClearSearch)
    }

    private fun fetchUsers() {
        val handler = CoroutineExceptionHandler { _, error ->
            emit(DisplayError(error.message.orEmpty()))
            updateState { copy(isRefreshing = false) }
            Log.e(ListViewModel::class.simpleName, "Fetch Error - ${error.message}", error)
        }
        viewModelScope.launch(handler) {
            updateState { copy(isRefreshing = true) }
            retrieveUsersUseCase()
                .collect { users ->
                    usersListCache.clear()
                    usersListCache.addAll(users)
                    updateState { copy(users = users, isRefreshing = users.isEmpty()) }
                }
        }
    }

    fun onSearchTextChange(searchQuery: String) {
        updateState { copy(users = listItemsFilter.filterItems(searchQuery, usersListCache)) }
    }
}
