package com.users.list.ui

import android.util.Log
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
                    view.displayUsersList(it)
                },
                onError = {
                    view.displayError(it.message.orEmpty())
                    logError(it)
                }
            ))
    }

    override fun onSearchTextChange(searchQuery: String) {
        compositeDisposable.add(userRepository.retrieveUsersLocally()
            .subscribeOn(schedulerProvider.io())
            .map { users -> listItemsFilter.filterItems(searchQuery, users) }
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onSuccess = {
                    view.displayUsersList(it)
                },
                onError = {
                    view.displayError(it.message.orEmpty())
                    logError(it)
                }
            ))
    }

    override fun onClear() {
        compositeDisposable.clear()
    }

    private fun logError(throwable: Throwable) {
        Log.e(ListPresenter::class.simpleName, "Error - ${throwable.message}", throwable)
    }
}
