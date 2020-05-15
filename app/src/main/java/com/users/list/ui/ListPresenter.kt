package com.users.list.ui

import android.util.Log
import com.users.list.model.domain.UserRepository
import com.users.list.ui.displayable.UserDisplayable
import com.users.list.ui.schedulers.SchedulerProvider
import com.users.list.utils.EMPTY
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

class ListPresenter(
  private val userRepository: UserRepository,
  private val schedulerProvider: SchedulerProvider,
  private val view: ListContract.View
) : ListContract.Presenter {
  private val compositeDisposable = CompositeDisposable()

  override fun fetchUsers() {
    getUsers()
  }

  override fun fetchUsersRepositories(userName: String) {
    compositeDisposable.add(userRepository.retrieveUserRepositories(userName)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .subscribeBy(
        onNext = { repositories ->
          view.updateUserListItem(userName, formatRepositories(repositories))
        },
        onError = {
          logError(it)
        }
      )
    )
  }

  override fun filterUsers(searchQuery: String?) {
    getUsers {
      val query = searchQuery ?: EMPTY
      it.name.contains(query) || it.repositoriesNames.contains(query)
    }
  }

  override fun releaseResources() {
    compositeDisposable.clear()
  }

  private fun getUsers(filterAction: (UserDisplayable) -> Boolean = { true }) {
    compositeDisposable.add(userRepository.retrieveUsers()
      .subscribeOn(schedulerProvider.io())
      .map { users -> users.map { UserDisplayable(it.name, it.avatarUrl, EMPTY) } }
      .map { users -> users.filter { filterAction.invoke(it) } }
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

  private fun logError(it: Throwable) {
    Log.e(ListPresenter::class.simpleName, "Api error", it)
  }

  private fun formatRepositories(repositories: List<String>): String {
    val formattedText = StringBuilder()
    repositories.forEachIndexed { index, element ->
      if (index != repositories.lastIndex) {
        formattedText.append("$element, ")
      } else {
        formattedText.append(element)
      }
    }
    return formattedText.toString()
  }
}