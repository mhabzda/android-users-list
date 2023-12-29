package com.mhabzda.userlist.ui

import app.cash.turbine.test
import com.mhabzda.userlist.domain.RetrieveUsersUseCase
import com.mhabzda.userlist.testutils.TestUserData.firstTestUserEntity
import com.mhabzda.userlist.testutils.TestUserData.secondTestUserEntity
import com.mhabzda.userlist.ui.ListContract.ListEffect.ClearSearch
import com.mhabzda.userlist.ui.ListContract.ListEffect.DisplayError
import com.mhabzda.userlist.ui.ListContract.ListViewState
import com.mhabzda.userlist.ui.filter.ListItemsFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.givenBlocking
import org.mockito.kotlin.mock

class ListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val mockRetrieveUsersUseCase: RetrieveUsersUseCase = mock()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `GIVEN users available WHEN initializing THEN display users list`() = runTest {
        givenBlocking { mockRetrieveUsersUseCase.invoke() }.willReturn(
            flowOf(
                emptyList(),
                listOf(firstTestUserEntity, secondTestUserEntity),
            ),
        )

        val viewModel = createViewModel()

        viewModel.state.test {
            testDispatcher.scheduler.runCurrent()

            assertEquals(ListViewState(users = emptyList(), isRefreshing = false), awaitItem())
            assertEquals(ListViewState(users = emptyList(), isRefreshing = true), awaitItem())
            assertEquals(ListViewState(users = listOf(firstTestUserEntity, secondTestUserEntity), isRefreshing = false), awaitItem())
        }
    }

    @Test
    fun `GIVEN users not available WHEN initializing THEN display error`() = runTest {
        val errorMessage = "cannot retrieve users"
        givenBlocking { mockRetrieveUsersUseCase.invoke() }.willReturn(
            flow {
                emit(emptyList())
                throw Exception(errorMessage)
            },
        )

        val viewModel = createViewModel()

        viewModel.effects.test {
            testDispatcher.scheduler.runCurrent()
            assertEquals(DisplayError(errorMessage), awaitItem())
        }
        assertEquals(ListViewState(users = emptyList(), isRefreshing = false), viewModel.state.value)
    }

    @Test
    fun `GIVEN users change WHEN refreshing THEN display new users and clear search`() = runTest {
        givenBlocking { mockRetrieveUsersUseCase.invoke() }.willReturn(
            flowOf(emptyList(), listOf(firstTestUserEntity, secondTestUserEntity)),
        )
        val viewModel = createViewModel()
        testDispatcher.scheduler.runCurrent()

        givenBlocking { mockRetrieveUsersUseCase.invoke() }.willReturn(
            flowOf(emptyList(), listOf(firstTestUserEntity)),
        )
        viewModel.onRefresh()

        viewModel.effects.test {
            testDispatcher.scheduler.runCurrent()
            assertEquals(ClearSearch, awaitItem())
        }
        assertEquals(ListViewState(users = listOf(firstTestUserEntity), isRefreshing = false), viewModel.state.value)
    }

    @Test
    fun `WHEN search text is changed THEN filter users properly`() {
        givenBlocking { mockRetrieveUsersUseCase.invoke() }.willReturn(
            flowOf(emptyList(), listOf(firstTestUserEntity, secondTestUserEntity)),
        )
        val viewModel = createViewModel()
        testDispatcher.scheduler.runCurrent()

        viewModel.onSearchTextChange("john")
        assertEquals(ListViewState(users = listOf(firstTestUserEntity)), viewModel.state.value)
        viewModel.onSearchTextChange("")
        assertEquals(ListViewState(users = listOf(firstTestUserEntity, secondTestUserEntity)), viewModel.state.value)
    }

    private fun createViewModel() = ListViewModel(
        retrieveUsersUseCase = mockRetrieveUsersUseCase,
        listItemsFilter = ListItemsFilter(),
    )
}
