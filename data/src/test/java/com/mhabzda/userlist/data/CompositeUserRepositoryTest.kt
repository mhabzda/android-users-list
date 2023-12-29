package com.mhabzda.userlist.data

import app.cash.turbine.test
import com.mhabzda.userlist.data.api.RemoteUserRepository
import com.mhabzda.userlist.data.database.LocalUserRepository
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
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
    fun `GIVEN no local data cached WHEN retrieve users THEN emit empty list and remote items`() = runTest {
        val repository = createRepository(
            localUserRepository = mock {
                onBlocking { retrieveUsers() } doReturn emptyList()
            },
            remoteUserRepository = mock {
                onBlocking { retrieveUsers() } doReturn listOf(firstUserEntity, secondUserEntity)
            }
        )

        repository.retrieveUsers().test {
            assertEquals(emptyList<UserEntity>(), awaitItem())
            assertEquals(listOf(firstUserEntity, secondUserEntity), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN local and remote data are the same but in different order WHEN retrieve users THEN emit only single list`() = runTest {
        val repository = createRepository(
            localUserRepository = mock {
                onBlocking { retrieveUsers() } doReturn listOf(firstUserEntity, secondUserEntity)
            },
            remoteUserRepository = mock {
                onBlocking { retrieveUsers() } doReturn listOf(secondUserEntity, firstUserEntity)
            }
        )

        repository.retrieveUsers().test {
            assertEquals(listOf(firstUserEntity, secondUserEntity), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN cannot fetch remote data WHEN retrieve users THEN emit local list and error`() = runTest {
        val errorMessage = "cannot fetch remote data"
        val repository = createRepository(
            localUserRepository = mock {
                onBlocking { retrieveUsers() } doReturn listOf(firstUserEntity, secondUserEntity)
            },
            remoteUserRepository = mock {
                onBlocking { retrieveUsers() } doThrow RuntimeException(errorMessage)
            }
        )

        repository.retrieveUsers().test {
            assertEquals(listOf(firstUserEntity, secondUserEntity), awaitItem())
            assertEquals(errorMessage, awaitError().message)
        }
    }

    @Test
    fun `GIVEN cannot fetch local data WHEN retrieve users THEN emit and error`() = runTest {
        val errorMessage = "cannot fetch local data"
        val repository = createRepository(
            localUserRepository = mock {
                onBlocking { retrieveUsers() } doThrow RuntimeException(errorMessage)
            },
            remoteUserRepository = mock(),
        )

        repository.retrieveUsers().test {
            assertEquals(errorMessage, awaitError().message)
        }
    }

    @Test
    fun `GIVEN can fetch only local data WHEN retrieve users THEN do not save users`() = runTest {
        val localRepository: LocalUserRepository = mock {
            onBlocking { retrieveUsers() } doReturn listOf(firstUserEntity, secondUserEntity)
        }
        val repository = createRepository(
            localUserRepository = localRepository,
            remoteUserRepository = mock {
                onBlocking { retrieveUsers() } doThrow RuntimeException("cannot fetch remote data")
            }
        )

        runCatching { repository.retrieveUsers().collect() }

        verify(localRepository, never()).insertUsers(any())
        verify(localRepository, never()).insertRepositories(any(), any())
    }

    @Test
    fun `GIVEN can fetch remote users WHEN retrieve users THEN insert items to local repository`() = runTest {
        val localRepository: LocalUserRepository = mock {
            onBlocking { retrieveUsers() } doReturn emptyList()
        }
        val repository = createRepository(
            localUserRepository = localRepository,
            remoteUserRepository = mock {
                onBlocking { retrieveUsers() } doReturn listOf(firstUserEntity, secondUserEntity)
            }
        )

        repository.retrieveUsers().collect()

        verify(localRepository).insertUsers(listOf(firstUserEntity, secondUserEntity))
        verify(localRepository).insertRepositories(firstName, firstRepos)
        verify(localRepository).insertRepositories(secondName, secondRepos)
    }

    private fun createRepository(localUserRepository: LocalUserRepository, remoteUserRepository: RemoteUserRepository) =
        CompositeUserRepository(
            localUserRepository = localUserRepository,
            remoteUserRepository = remoteUserRepository,
        )
}
