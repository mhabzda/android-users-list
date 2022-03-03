package com.users.list.ui

import android.util.Log
import com.users.list.model.domain.UserEntity
import com.users.list.model.domain.UserRepository
import com.users.list.ui.filter.ListItemsFilter
import com.users.list.ui.schedulers.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ListPresenter @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val view: ListContract.View,
    private val listItemsFilter: ListItemsFilter
) : ListContract.Presenter {

    private val compositeDisposable = CompositeDisposable()
    private val usersList: MutableList<UserEntity> = mutableListOf()

    override fun onCreate() {
        fetchUsers()
    }

    override fun onRefresh() {
        fetchUsers()
        view.clearSearch()
    }

    private fun fetchUsers() {
        compositeDisposable.add(userRepository.retrieveUsers()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui(), true)
            .doOnSubscribe { view.toggleRefreshing(isRefreshing = true) }
            .doOnTerminate { view.toggleRefreshing(isRefreshing = false) }
            .subscribeBy(
                onNext = {
                    usersList.clear()
                    usersList.addAll(it)
                    view.displayUsersList(it)
                },
                onError = {
                    view.displayError(it.message.orEmpty())
                    Log.e(ListPresenter::class.simpleName, "Error - ${it.message}", it)
                }
            ))
    }

    override fun onSearchTextChange(searchQuery: String) {
        view.displayUsersList(listItemsFilter.filterItems(searchQuery, usersList))
    }

    override fun onClear() {
        compositeDisposable.clear()
    }
}
