package com.users.list.ui

import com.users.list.model.domain.UserEntity
import com.users.list.model.domain.UserRepository
import com.users.list.testutils.TestUserData.firstTestUserEntity
import com.users.list.testutils.TestUserData.secondTestUserEntity
import com.users.list.ui.filter.ListItemsFilter
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.jupiter.api.Test
import org.mockito.kotlin.InOrderOnType
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ListPresenterTest {

    private val testSchedulerProvider = TestSchedulerProvider()
    private val view: ListContract.View = mock()

    @Test
    fun `given users available when on create then display users list`() {
        val presenter = createPresenter(userRepository = mock {
            on { retrieveUsers() } doReturn Observable.just(listOf(firstTestUserEntity, secondTestUserEntity))
        })

        presenter.onCreate()
        testSchedulerProvider.triggerActions()

        verify(view).displayUsersList(listOf(firstTestUserEntity, secondTestUserEntity))
    }

    @Test
    fun `given users not available when on create then display error`() {
        val errorMessage = "cannot retrieve users"
        val presenter = createPresenter(userRepository = mock {
            on { retrieveUsers() } doReturn Observable.error(Throwable(errorMessage))
        })

        presenter.onCreate()
        testSchedulerProvider.triggerActions()

        verify(view).displayError(errorMessage)
    }

    @Test
    fun `toggle loading on create`() {
        val presenter = createPresenter(userRepository = mock {
            on { retrieveUsers() } doReturn Observable.just(listOf(firstTestUserEntity, secondTestUserEntity))
        })

        presenter.onCreate()
        testSchedulerProvider.triggerActions()

        val inOrder = InOrderOnType(view)
        inOrder.verify().toggleRefreshing(true)
        inOrder.verify().toggleRefreshing(false)
    }

    @Test
    fun `fetch users and clear search on refresh`() {
        val presenter = createPresenter(userRepository = mock {
            on { retrieveUsers() } doReturn Observable.just(listOf(firstTestUserEntity, secondTestUserEntity))
        })
        presenter.onCreate()
        testSchedulerProvider.triggerActions()

        presenter.onRefresh()
        testSchedulerProvider.triggerActions()

        verify(view, times(2)).displayUsersList(listOf(firstTestUserEntity, secondTestUserEntity))
        verify(view).clearSearch()
    }

    @Test
    fun `display filtered users when text is changed`() {
        val presenter = createPresenter(userRepository = mock {
            on { retrieveUsers() } doReturn Observable.just(listOf(firstTestUserEntity, secondTestUserEntity))
        })
        presenter.onCreate()
        testSchedulerProvider.triggerActions()

        presenter.onSearchTextChange("john")

        verify(view).displayUsersList(listOf(firstTestUserEntity))
    }

    @Test
    fun `clear observers and ignore next values when on clear`() {
        val usersSubject = PublishSubject.create<List<UserEntity>>()
        val presenter = createPresenter(userRepository = mock {
            on { retrieveUsers() } doReturn usersSubject.hide()
        })

        presenter.onCreate()
        testSchedulerProvider.triggerActions()
        usersSubject.onNext(listOf(firstTestUserEntity, secondTestUserEntity))
        testSchedulerProvider.triggerActions()
        clearInvocations(view)

        presenter.onClear()

        usersSubject.onNext(listOf(firstTestUserEntity, secondTestUserEntity))
        testSchedulerProvider.triggerActions()
        verify(view, never()).displayUsersList(any())
    }

    private fun createPresenter(userRepository: UserRepository = mock()) =
        ListPresenter(
            userRepository = userRepository,
            schedulerProvider = testSchedulerProvider,
            view = view,
            listItemsFilter = ListItemsFilter()
        )
}
