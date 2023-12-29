package com.mhabzda.userlist.data.database

import com.mhabzda.userlist.data.database.dao.RepositoryDao
import com.mhabzda.userlist.data.database.dao.UserDao
import com.mhabzda.userlist.data.database.mapper.UserLocalMapper
import com.mhabzda.userlist.data.database.model.UserLocalDto
import com.mhabzda.userlist.data.database.model.UserRepositoryLocalDto
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class LocalUserRepositoryTest {
    private val firstLogin = "john"
    private val secondLogin = "micheal"
    private val firstUserDto = UserLocalDto(firstLogin, "url")
    private val secondUserDto = UserLocalDto(secondLogin, "url")
    private val firstRepo = UserRepositoryLocalDto(firstLogin, "repo1")
    private val secondRepo = UserRepositoryLocalDto(secondLogin, "repo2")
    private val thirdRepo = UserRepositoryLocalDto(secondLogin, "repo3")

    private val firstUserEntity = UserEntity(firstLogin, "url", listOf("repo1"))
    private val secondUserEntity = UserEntity(secondLogin, "url", listOf("repo2", "repo3"))

    @Test
    fun `GIVEN local data present WHEN retrieve users THEN emit local data`() = runTest {
        val repository = createRepository(
            userDao = mock {
                onBlocking { getUsers() } doReturn listOf(firstUserDto, secondUserDto)
            },
            repositoryDao = mock {
                onBlocking { getRepositories(firstLogin) } doReturn listOf(firstRepo)
                onBlocking { getRepositories(secondLogin) } doReturn listOf(secondRepo, thirdRepo)
            }
        )

        val result = repository.retrieveUsers()

        assertEquals(listOf(firstUserEntity, secondUserEntity), result)
    }

    @Test
    fun `GIVEN no local data present WHEN retrieve users THEN emit empty list`() = runTest {
        val repository = createRepository(
            userDao = mock {
                onBlocking { getUsers() } doReturn emptyList()
            },
            repositoryDao = mock(),
        )

        val result = repository.retrieveUsers()

        assertEquals(emptyList<UserEntity>(), result)
    }

    @Test
    fun `GIVEN users error WHEN retrieve users THEN emit error`() = runTest {
        val errorMessage = "error while fetching local data"
        val repository = createRepository(
            userDao = mock {
                onBlocking { getUsers() } doThrow RuntimeException(errorMessage)
            },
            repositoryDao = mock(),
        )

        val result = runCatching { repository.retrieveUsers() }

        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `GIVEN repositories error WHEN retrieve users THEN emit error`() = runTest {
        val errorMessage = "error while fetching local data"
        val repository = createRepository(
            userDao = mock {
                onBlocking { getUsers() } doReturn listOf(firstUserDto, secondUserDto)
            },
            repositoryDao = mock {
                onBlocking { getRepositories(firstLogin) } doReturn listOf(firstRepo)
                onBlocking { getRepositories(secondLogin) } doThrow RuntimeException(errorMessage)
            }
        )

        val result = runCatching { repository.retrieveUsers() }

        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `WHEN insert users THEN insert users using DAO`() = runTest {
        val userDao: UserDao = mock()
        val repository = createRepository(userDao = userDao, repositoryDao = mock())

        repository.insertUsers(listOf(firstUserEntity, secondUserEntity))

        verify(userDao).insert(firstUserDto, secondUserDto)
    }

    @Test
    fun `WHEN insert repositories THEN insert repositories using DAO`() = runTest {
        val repositoryDao: RepositoryDao = mock()
        val repository = createRepository(userDao = mock(), repositoryDao = repositoryDao)

        repository.insertRepositories(firstLogin, listOf("repo1"))
        repository.insertRepositories(secondLogin, listOf("repo2", "repo3"))

        verify(repositoryDao).insert(firstRepo)
        verify(repositoryDao).insert(secondRepo, thirdRepo)
    }

    private fun createRepository(userDao: UserDao, repositoryDao: RepositoryDao) =
        LocalUserRepository(
            userDao = userDao,
            repositoryDao = repositoryDao,
            userLocalMapper = UserLocalMapper(),
        )
}
