package com.users.list.ui

import com.nhaarman.mockitokotlin2.InOrderOnType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.users.list.model.domain.UserEntity
import com.users.list.model.domain.UserRepository
import com.users.list.testutils.TestUserData.firstTestUserEntity
import com.users.list.testutils.TestUserData.secondTestUserEntity
import com.users.list.ui.filter.ListItemsFilter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.Test

class ListPresenterTests {
  private val testSchedulerProvider = TestSchedulerProvider()
  private val view: ListContract.View = mock()

  @Test
  fun `given users available when retrieve users then display users list`() {
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsers() } doReturn Observable.just(listOf(firstTestUserEntity, secondTestUserEntity))
    })

    presenter.fetchUsers()
    testSchedulerProvider.triggerActions()

    verify(view).displayUserList(listOf(firstTestUserEntity, secondTestUserEntity))
  }

  @Test
  fun `given users available when retrieve users then toggle loading`() {
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsers() } doReturn Observable.just(listOf(firstTestUserEntity, secondTestUserEntity))
    })

    presenter.fetchUsers()
    testSchedulerProvider.triggerActions()

    val inOrder = InOrderOnType(view)
    inOrder.verify().toggleRefreshing(true)
    inOrder.verify().toggleRefreshing(false)
  }

  @Test
  fun `given users not available when retrieve users then display error`() {
    val errorMessage = "cannot retrieve users"
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsers() } doReturn Observable.error(Throwable(errorMessage))
    })

    presenter.fetchUsers()
    testSchedulerProvider.triggerActions()

    verify(view).displayError(errorMessage)
  }

  @Test
  fun `given users not available when retrieve users then toggle loading`() {
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsers() } doReturn Observable.error(Throwable("cannot retrieve users"))
    })

    presenter.fetchUsers()
    testSchedulerProvider.triggerActions()

    val inOrder = InOrderOnType(view)
    inOrder.verify().toggleRefreshing(true)
    inOrder.verify().toggleRefreshing(false)
  }

  @Test
  fun `given can retrieve users locally when filter items then display only filtered users`() {
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsersLocally() } doReturn Single.just(listOf(firstTestUserEntity, secondTestUserEntity))
    })

    presenter.filterUsers("john")
    testSchedulerProvider.triggerActions()

    verify(view).displayUserList(listOf(firstTestUserEntity))
  }

  @Test
  fun `given cannot retrieve users locally when filter items then display error`() {
    val errorMessage = "error while fetching users locally"
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsersLocally() } doReturn Single.error(Throwable(errorMessage))
    })

    presenter.filterUsers("john")
    testSchedulerProvider.triggerActions()

    verify(view).displayError(errorMessage)
  }

  @Test
  fun `given retrieving users when release resources then ignore next values`() {
    val usersSubject = PublishSubject.create<List<UserEntity>>()
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsers() } doReturn usersSubject.hide()
    })

    presenter.fetchUsers()
    testSchedulerProvider.triggerActions()
    usersSubject.onNext(listOf(firstTestUserEntity, secondTestUserEntity))
    testSchedulerProvider.triggerActions()
    clearInvocations(view)

    presenter.releaseResources()

    usersSubject.onNext(listOf(firstTestUserEntity, secondTestUserEntity))
    testSchedulerProvider.triggerActions()
    verify(view, never()).displayUserList(any())
  }

  private fun createPresenter(userRepository: UserRepository = mock()): ListPresenter {
    return ListPresenter(
      userRepository = userRepository,
      schedulerProvider = testSchedulerProvider,
      view = view,
      listItemsFilter = ListItemsFilter()
    )
  }
}