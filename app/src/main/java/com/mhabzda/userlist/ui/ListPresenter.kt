package com.mhabzda.userlist.ui

import android.util.Log
import com.mhabzda.userlist.domain.UserRepository
import com.mhabzda.userlist.domain.model.UserEntity
import com.mhabzda.userlist.ui.filter.ListItemsFilter
import com.mhabzda.userlist.ui.schedulers.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ListPresenter @Inject constructor(
    private val userRepository: UserRepository,
    private val schedulerProvider: SchedulerProvider,
    private val listItemsFilter: ListItemsFilter,
) : ListContract.Presenter {

    private var view: ListContract.View? = null

    private val compositeDisposable = CompositeDisposable()
    private val usersList: MutableList<UserEntity> = mutableListOf()

    override fun onCreate(view: ListContract.View) {
        this.view = view
        fetchUsers()
    }

    override fun onRefresh() {
        fetchUsers()
        view?.clearSearch()
    }

    private fun fetchUsers() {
        compositeDisposable.add(userRepository.retrieveUsers()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui(), true)
            .doOnSubscribe { view?.toggleRefreshing(isRefreshing = true) }
            .doOnTerminate { view?.toggleRefreshing(isRefreshing = false) }
            .subscribeBy(
                onNext = {
                    usersList.clear()
                    usersList.addAll(it)
                    view?.displayUsersList(it)
                },
                onError = {
                    view?.displayError(it.message.orEmpty())
                    Log.e(ListPresenter::class.simpleName, "Error - ${it.message}", it)
                }
            ))
    }

    override fun onSearchTextChange(searchQuery: String) {
        view?.displayUsersList(listItemsFilter.filterItems(searchQuery, usersList))
    }

    override fun onClear() {
        this.view = null
        compositeDisposable.clear()
    }
}
