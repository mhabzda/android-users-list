package com.users.list.ui

import android.util.Log
import com.users.list.model.domain.UserRepository
import com.users.list.ui.displayable.UserDisplayable
import com.users.list.ui.schedulers.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ListPresenter(
  private val userRepository: UserRepository,
  private val schedulerProvider: SchedulerProvider,
  private val view: ListContract.View
) : ListContract.Presenter {
  private val compositeDisposable = CompositeDisposable()

  override fun fetchUsers() {
    compositeDisposable.add(userRepository.retrieveUsers()
      .subscribeOn(schedulerProvider.io())
      .map { users -> users.map { UserDisplayable(it.name, it.avatarUrl, emptyList()) } }
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

  override fun fetchUsersRepositories(userName: String) {
    compositeDisposable.add(userRepository.retrieveUserRepositories(userName)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribeBy(
        onNext = { repositories ->
          view.updateUserListItem(userName, repositories)
        },
        onError = {
          logError(it)
        }
      )
    )
  }

  override fun releaseResources() {
    compositeDisposable.clear()
  }

  private fun logError(it: Throwable) {
    Log.e(ListPresenter::class.simpleName, "Api error", it)
  }
}