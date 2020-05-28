package com.users.list.ui

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.users.list.model.domain.UserEntity
import com.users.list.model.domain.UserRepository
import com.users.list.ui.schedulers.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import org.junit.Test

class ListPresenterTests {
  private val testScheduler = TestScheduler()
  private val schedulerProvider = object : SchedulerProvider {
    override fun io(): Scheduler {
      return testScheduler
    }

    override fun ui(): Scheduler {
      return testScheduler
    }
  }
  private val view: ListContract.View = mock()

  @Test
  fun `given users available when fetch users then display users list`() {
    val presenter = createPresenter(userRepository = mock {
      on { retrieveUsers() } doReturn Observable.just(listOf(UserEntity("a", "b", listOf("repo"))))
    })

    presenter.fetchUsers()
    testScheduler.triggerActions()

    verify(view).displayUserList(listOf(UserEntity("a", "b", listOf("repo"))))
  }

  @Test
  fun `given users unavailable when fetch users then log error`() {

  }

  // all logic inside presenter can be easily tested. I'm not doing that because I don't have enough time.
  // some operations like filtering or mapping can be even extracted
  // to separate classes (like mappers) and tested separately

  // Additionally, I should test logic inside CompositeUserRepository to make sure merge logic is working correctly.
  // But I have no time for that.

  private fun createPresenter(userRepository: UserRepository = mock()): ListPresenter {
    return ListPresenter(
      userRepository = userRepository,
      schedulerProvider = schedulerProvider,
      view = view
    )
  }
}