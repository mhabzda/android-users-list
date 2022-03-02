package com.users.list.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.users.list.model.api.RemoteRepository
import com.users.list.model.database.LocalRepository
import com.users.list.model.domain.CompositeUserRepository
import com.users.list.model.domain.UserEntity
import io.reactivex.Single
import org.junit.jupiter.api.Test

class CompositeUserRepositoryTest {
    private val firstName = "Dennis"
    private val secondName = "micheal"
    private val firstRepos = listOf("repo1", "jsonSerialize")
    private val secondRepos = listOf("repo2")
    private val firstUserEntity = UserEntity(firstName, "url", firstRepos)
    private val secondUserEntity = UserEntity(secondName, "url", secondRepos)

    @Test
    fun `given no local data cached when retrieve users then emit empty list and remote items`() {
        val repository = createRepository(
            localUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(emptyList())
            },
            remoteUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
            }
        )

        repository.retrieveUsers()
            .test()
            .assertValues(listOf(), listOf(firstUserEntity, secondUserEntity))
    }

    @Test
    fun `given local and remote data are the same but in different order when retrieve users then emit only single list`() {
        val repository = createRepository(
            localUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
            },
            remoteUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(secondUserEntity, firstUserEntity))
            }
        )

        repository.retrieveUsers()
            .test()
            .assertValue(listOf(firstUserEntity, secondUserEntity))
    }

    @Test
    fun `given cannot fetch remote data when retrieve users then emit local list and error`() {
        val error = Throwable("cannot fetch remote data")
        val repository = createRepository(
            localUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
            },
            remoteUserRepository = mock {
                on { retrieveUsers() } doReturn Single.error(error)
            }
        )

        repository.retrieveUsers()
            .test()
            .assertValue(listOf(firstUserEntity, secondUserEntity))
            .assertError(error)
    }

    @Test
    fun `given cannot fetch local data when retrieve users then emit remote list and error`() {
        val error = Throwable("cannot fetch local data")
        val repository = createRepository(
            localUserRepository = mock {
                on { retrieveUsers() } doReturn Single.error(error)
            },
            remoteUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
            }
        )

        repository.retrieveUsers()
            .test()
            .assertValue(listOf(firstUserEntity, secondUserEntity))
            .assertError(error)
    }

    @Test
    fun `given can fetch only local data when retrieve users then do not save users`() {
        val localRepository: LocalRepository = mock {
            on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
        }
        val repository = createRepository(
            localUserRepository = localRepository,
            remoteUserRepository = mock {
                on { retrieveUsers() } doReturn Single.error(Throwable("cannot fetch remote data"))
            }
        )

        repository.retrieveUsers().test()

        verify(localRepository, never()).insertUsers(any())
        verify(localRepository, never()).insertRepositories(any(), any())
    }

    @Test
    fun `given can fetch remote users when retrieve users then insert items to local repository`() {
        val localRepository: LocalRepository = mock {
            on { retrieveUsers() } doReturn Single.just(emptyList())
        }
        val repository = createRepository(
            localUserRepository = localRepository,
            remoteUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
            }
        )

        repository.retrieveUsers().test()

        verify(localRepository).insertUsers(listOf(firstUserEntity, secondUserEntity))
        verify(localRepository).insertRepositories(firstName, firstRepos)
        verify(localRepository).insertRepositories(secondName, secondRepos)
    }

    @Test
    fun `given local data present when retrieve users locally then emit users`() {
        val repository = createRepository(
            localUserRepository = mock {
                on { retrieveUsers() } doReturn Single.just(listOf(firstUserEntity, secondUserEntity))
            },
            remoteUserRepository = mock()
        )

        repository.retrieveUsersLocally()
            .test()
            .assertValue(listOf(firstUserEntity, secondUserEntity))
    }

    @Test
    fun `given no local data when retrieve users locally then emit error`() {
        val error = Throwable("no local users")
        val repository = createRepository(
            localUserRepository = mock {
                on { retrieveUsers() } doReturn Single.error(error)
            },
            remoteUserRepository = mock()
        )

        repository.retrieveUsersLocally()
            .test()
            .assertError(error)
    }

    private fun createRepository(
        localUserRepository: LocalRepository,
        remoteUserRepository: RemoteRepository
    ): CompositeUserRepository {
        return CompositeUserRepository(localUserRepository, remoteUserRepository)
    }
}
