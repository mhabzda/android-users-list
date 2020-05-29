package com.users.list.ui

import android.util.Log
import com.users.list.model.domain.UserRepository
import com.users.list.ui.filter.ListItemsFilter
import com.users.list.ui.schedulers.SchedulerProvider
import com.users.list.utils.EMPTY
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

  override fun fetchUsers() {
    compositeDisposable.add(userRepository.retrieveUsers()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui(), true)
      .doOnSubscribe { view.toggleRefreshing(isRefreshing = true) }
      .doOnTerminate { view.toggleRefreshing(isRefreshing = false) }
      .subscribeBy(
        onNext = {
          view.displayUserList(it)
        },
        onError = {
          view.displayError(it.message ?: EMPTY)
          logError(it)
        }
      ))
  }

  override fun filterUsers(searchQuery: String) {
    compositeDisposable.add(userRepository.retrieveUsersLocally()
      .subscribeOn(schedulerProvider.io())
      .map { users -> listItemsFilter.filterItems(searchQuery, users) }
      .observeOn(schedulerProvider.ui())
      .subscribeBy(
        onSuccess = {
          view.displayUserList(it)
        },
        onError = {
          view.displayError(it.message ?: EMPTY)
          logError(it)
        }
      ))
  }

  override fun releaseResources() {
    compositeDisposable.clear()
  }

  private fun logError(throwable: Throwable) {
    Log.e(ListPresenter::class.simpleName, "Error - ${throwable.message}", throwable)
  }
}