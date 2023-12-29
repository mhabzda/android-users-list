package com.mhabzda.userlist.data.api

import com.mhabzda.userlist.data.api.mapper.UserRemoteMapper
import com.mhabzda.userlist.data.api.model.UserRemoteDto
import com.mhabzda.userlist.data.api.model.UserRepositoryRemoteDto
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock

class RemoteUserRepositoryTest {

    @Test
    fun `GIVEN can retrieve everything WHEN retrieving users THEN return list of users`() = runTest {
        val repository = createRepository(userApi = mockUserApi())

        val result = repository.retrieveUsers()

        assertEquals(listOf(firstUserEntity, secondUserEntity, thirdUserEntity), result)
    }

    @Test
    fun `GIVEN cannot retrieve user list WHEN retrieving users THEN throw an error`() = runTest {
        val errorMessage = "cannot fetch users"
        val repository = createRepository(userApi = mock {
            onBlocking { fetchUsers() } doThrow RuntimeException(errorMessage)
        })

        val result = runCatching { repository.retrieveUsers() }

        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `GIVEN multiple repositories call fails WHEN retrieving users THEN throw an error`() = runTest {
        val errorMessage = "cannot fetch a repository"
        val repository = createRepository(userApi = mockUserApiWithRepositoryError(RuntimeException(errorMessage)))

        val result = runCatching { repository.retrieveUsers() }

        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    private fun createRepository(userApi: UserApi) =
        RemoteUserRepository(
            userApi = userApi,
            userRemoteMapper = UserRemoteMapper()
        )

    private val firstUserDto = UserRemoteDto("user1", "url")
    private val secondUserDto = UserRemoteDto("user2", "url")
    private val thirdUserDto = UserRemoteDto("user3", "url")
    private val firstRepositoryDto = UserRepositoryRemoteDto("repo1")
    private val secondRepositoryDto = UserRepositoryRemoteDto("repo2")
    private val thirdRepositoryDto = UserRepositoryRemoteDto("repo3")
    private val firstUserEntity = UserEntity("user1", "url", listOf("repo1"))
    private val secondUserEntity = UserEntity("user2", "url", listOf("repo2"))
    private val thirdUserEntity = UserEntity("user3", "url", listOf("repo3"))

    private fun mockUserApi(): UserApi =
        mock {
            onBlocking { fetchUsers() } doReturn listOf(firstUserDto, secondUserDto, thirdUserDto)
            onBlocking { fetchUserRepository("user1") } doReturn listOf(firstRepositoryDto)
            onBlocking { fetchUserRepository("user2") } doReturn listOf(secondRepositoryDto)
            onBlocking { fetchUserRepository("user3") } doReturn listOf(thirdRepositoryDto)
        }

    private fun mockUserApiWithRepositoryError(error: Exception): UserApi =
        mock {
            onBlocking { fetchUsers() } doReturn listOf(
                firstUserDto,
                secondUserDto,
                thirdUserDto,
                UserRemoteDto("user4", "url"),
                UserRemoteDto("user5", "url"),
            )
            onBlocking { fetchUserRepository("user1") } doReturn listOf(firstRepositoryDto)
            onBlocking { fetchUserRepository("user2") } doReturn listOf(secondRepositoryDto)
            onBlocking { fetchUserRepository("user3") } doThrow error
        }
}
