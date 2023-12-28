package com.mhabzda.userlist.data

import android.annotation.SuppressLint
import com.mhabzda.userlist.data.api.RemoteRepository
import com.mhabzda.userlist.data.database.LocalRepository
import com.mhabzda.userlist.domain.model.UserEntity
import io.reactivex.Single
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

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

    @SuppressLint("CheckResult")
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

    @SuppressLint("CheckResult")
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

    private fun createRepository(localUserRepository: LocalRepository, remoteUserRepository: RemoteRepository) =
        CompositeUserRepository(localUserRepository, remoteUserRepository)
}
