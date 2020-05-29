package com.users.list.api

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.api.UserApi
import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import com.users.list.model.api.mapper.UserRemoteMapper
import com.users.list.model.domain.UserEntity
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class RemoteUserRepositoryTest {
  private val testScheduler = TestScheduler()

  @Test
  fun `given can retrieve all info when retrieving users then return list of users`() {
    val repository = createRepository(userApi = mockWorkingUserApi())

    repository.retrieveUsers()
      .test()
      .assertValue(listOf(firstUserEntity, secondUserEntity, thirdUserEntity))
  }

  @Test
  fun `given cannot retrieve user list when retrieving users then throw error`() {
    val error = Throwable("cannot fetch users")
    val repository = createRepository(userApi = mock {
      on { fetchUsers() } doReturn Single.error(error)
    })

    repository.retrieveUsers()
      .test()
      .assertError(error)
  }

  @Test
  fun `given multiple repositories call fails when retrieving users then throw single error after delay`() {
    val repository = createRepository(userApi = mockUserApiWithErrorsDelay())

    val testObserver = repository.retrieveUsers().test()

    testObserver.assertNoErrors()
    testScheduler.advanceTimeBy(110, TimeUnit.MILLISECONDS)
    testObserver.assertError { it.message == "403 - rate limit" }
  }

  private fun createRepository(userApi: UserApi): RemoteUserRepository {
    return RemoteUserRepository(
      userApi = userApi,
      userRemoteMapper = UserRemoteMapper()
    )
  }

  private val firstUserDto = UserRemoteDto("user1", "url")
  private val secondUserDto = UserRemoteDto("user2", "url")
  private val thirdUserDto = UserRemoteDto("user3", "url")
  private val firstRepositoryDto = UserRepositoryRemoteDto("repo1")
  private val secondRepositoryDto = UserRepositoryRemoteDto("repo2")
  private val thirdRepositoryDto = UserRepositoryRemoteDto("repo3")
  private val firstUserEntity = UserEntity("user1", "url", listOf("repo1"))
  private val secondUserEntity = UserEntity("user2", "url", listOf("repo2"))
  private val thirdUserEntity = UserEntity("user3", "url", listOf("repo3"))

  private fun mockWorkingUserApi(): UserApi {
    return mock {
      on { fetchUsers() } doReturn Single.just(listOf(firstUserDto, secondUserDto, thirdUserDto))
      on { fetchUserRepository("user1") } doReturn Single.just(listOf(firstRepositoryDto))
      on { fetchUserRepository("user2") } doReturn Single.just(listOf(secondRepositoryDto))
      on { fetchUserRepository("user3") } doReturn Single.just(listOf(thirdRepositoryDto))
    }
  }

  private fun mockUserApiWithErrorsDelay(): UserApi {
    return mock {
      on { fetchUsers() } doReturn Single.just(
        listOf(firstUserDto, secondUserDto, thirdUserDto, UserRemoteDto("user4", "url"), UserRemoteDto("user5", "url"))
      )
      on { fetchUserRepository("user1") } doReturn Single.just(listOf(firstRepositoryDto))
      on { fetchUserRepository("user2") } doReturn Single.just(listOf(secondRepositoryDto))
      on { fetchUserRepository("user3") } doReturn Single.error(Throwable("403 - rate limit"))
      on { fetchUserRepository("user4") } doReturn getRepositoriesSingleWithDelay(Throwable("403 - rate limit"), 100)
      on { fetchUserRepository("user5") } doReturn getRepositoriesSingleWithDelay(Throwable("403 - rate limit"), 110)
    }
  }

  private fun getRepositoriesSingleWithDelay(error: Throwable, delayMillis: Long)
      : Single<List<UserRepositoryRemoteDto>> {
    return Single.just(Any())
      .delay(delayMillis, TimeUnit.MILLISECONDS, testScheduler)
      .flatMap { Single.error<List<UserRepositoryRemoteDto>>(error) }
  }
}