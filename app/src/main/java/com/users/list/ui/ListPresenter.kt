package com.users.list.ui

import android.util.Log
import com.users.list.model.domain.UserEntity
import com.users.list.model.domain.UserRepository
import com.users.list.ui.schedulers.SchedulerProvider
import com.users.list.utils.EMPTY
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class ListPresenter @Inject constructor(
  private val userRepository: UserRepository,
  private val schedulerProvider: SchedulerProvider,
  private val view: ListContract.View
) : ListContract.Presenter {
  private val compositeDisposable = CompositeDisposable()

  override fun fetchUsers() {
    compositeDisposable.add(userRepository.retrieveUsers()
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribeBy(
        onNext = {
          view.displayUserList(it)
        },
        onError = {
          logError(it)
        }
      ))
  }

  override fun filterUsers(searchQuery: String?) {
    compositeDisposable.add(userRepository.retrieveUsersLocally()
      .subscribeOn(schedulerProvider.io())
      .map { users -> filterItems(searchQuery, users) }
      .observeOn(schedulerProvider.ui())
      .subscribeBy(
        onSuccess = {
          view.displayUserList(it)
        },
        onError = {
          logError(it)
        }
      ))
  }

  private fun filterItems(searchQuery: String?, users: List<UserEntity>): List<UserEntity> {
    val query = searchQuery ?: EMPTY
    return users.filter { it.name.contains(query) || it.repositories.toString().contains(query) }
  }

  override fun releaseResources() {
    compositeDisposable.clear()
  }

  private fun logError(it: Throwable) {
    Log.e(ListPresenter::class.simpleName, "Api error", it)
  }
}