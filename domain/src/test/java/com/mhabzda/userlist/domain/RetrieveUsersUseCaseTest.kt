package com.mhabzda.userlist.domain

import app.cash.turbine.test
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.givenBlocking
import org.mockito.kotlin.mock

class RetrieveUsersUseCaseTest {

    private val firstUserEntity = UserEntity("Dennis", "url", listOf("repo1", "jsonSerialize"))
    private val secondUserEntity = UserEntity("micheal", "url", listOf("repo2"))

    private val mockUserRepository: UserRepository = mock()

    private val useCase = RetrieveUsersUseCase(
        userRepository = mockUserRepository,
    )

    @Test
    fun `GIVEN can retrieve users WHEN invoke THEN return users`() = runTest {
        givenBlocking { mockUserRepository.retrieveUsers() }.willReturn(flowOf(listOf(firstUserEntity, secondUserEntity)))

        useCase.invoke().test {
            assertEquals(listOf(firstUserEntity, secondUserEntity), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN cannot retrieve users WHEN invoke THEN throw an error`() = runTest {
        val errorMessage = "cannot retrieve users"
        givenBlocking { mockUserRepository.retrieveUsers() }.willReturn(flow { throw RuntimeException(errorMessage) })

        useCase.invoke().test {
            assertEquals(errorMessage, awaitError().message)
        }
    }
}
