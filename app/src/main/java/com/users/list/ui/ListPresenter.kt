package com.users.list.ui

import android.util.Log
import com.users.list.model.UserRepository
import com.users.list.model.domain.UserEntity
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
      .map { users ->
        users.map { UserEntity(it.login, it.avatarUrl, emptyList()) }
      }
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

  override fun fetchUsersRepositories(userName: String) {
    compositeDisposable.add(userRepository.retrieveUserRepositories(userName)
      .observeOn(schedulerProvider.ui())
      .subscribeBy(
        onSuccess = { repositories ->
          view.updateUserListItem(userName, repositories.map { it.name })
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