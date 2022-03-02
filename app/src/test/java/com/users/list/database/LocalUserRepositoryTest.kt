package com.users.list.database

import com.users.list.model.database.LocalUserRepository
import com.users.list.model.database.dao.RepositoryDao
import com.users.list.model.database.dao.UserDao
import com.users.list.model.database.dtos.UserLocalDto
import com.users.list.model.database.dtos.UserRepositoryLocalDto
import com.users.list.model.database.mapper.UserLocalMapper
import com.users.list.model.domain.UserEntity
import io.reactivex.Single
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
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
    fun `given local data present when retrieve users then emit local data`() {
        val repository = createRepository(
            userDao = mock {
                on { getUsers() } doReturn Single.just(listOf(firstUserDto, secondUserDto))
            },
            repositoryDao = mock {
                on { getRepositories(firstLogin) } doReturn Single.just(listOf(firstRepo))
                on { getRepositories(secondLogin) } doReturn Single.just(listOf(secondRepo, thirdRepo))
            }
        )

        repository.retrieveUsers()
            .test()
            .assertValue(listOf(firstUserEntity, secondUserEntity))
    }

    @Test
    fun `given no local data present when retrieve users then emit empty list`() {
        val repository = createRepository(
            userDao = mock {
                on { getUsers() } doReturn Single.just(emptyList())
            },
            repositoryDao = mock()
        )

        repository.retrieveUsers()
            .test()
            .assertValue(emptyList())
    }

    @Test
    fun `given error when retrieve users then emit error`() {
        val error = Throwable("error while fetching local data")
        val repository = createRepository(
            userDao = mock {
                on { getUsers() } doReturn Single.error(error)
            },
            repositoryDao = mock()
        )

        repository.retrieveUsers()
            .test()
            .assertError(error)
    }

    @Test
    fun `when insert users then insert users using DAO`() {
        val userDao: UserDao = mock()
        val repository = createRepository(userDao = userDao, repositoryDao = mock())

        repository.insertUsers(listOf(firstUserEntity, secondUserEntity))

        verify(userDao).insert(firstUserDto, secondUserDto)
    }

    @Test
    fun `when insert repositories then insert repositories using DAO`() {
        val repositoryDao: RepositoryDao = mock()
        val repository = createRepository(userDao = mock(), repositoryDao = repositoryDao)

        repository.insertRepositories(firstLogin, listOf("repo1"))
        repository.insertRepositories(secondLogin, listOf("repo2", "repo3"))

        verify(repositoryDao).insert(firstRepo)
        verify(repositoryDao).insert(secondRepo, thirdRepo)
    }

    private fun createRepository(
        userDao: UserDao,
        repositoryDao: RepositoryDao
    ): LocalUserRepository {
        return LocalUserRepository(userDao, repositoryDao, UserLocalMapper())
    }
}
