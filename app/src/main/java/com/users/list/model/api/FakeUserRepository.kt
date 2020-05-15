package com.users.list.model.api

import com.users.list.model.UserRepository
import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class FakeUserRepository : UserRepository {
  override fun retrieveUsers(): Single<List<UserRemoteDto>> {
    return Single.just(
      listOf(
        UserRemoteDto("name1", "avatarUrl"),
        UserRemoteDto("name2", "avatarUrl"),
        UserRemoteDto("name3", "avatarUrl"),
        UserRemoteDto("name4", "avatarUrl"),
        UserRemoteDto("name5", "avatarUrl"),
        UserRemoteDto("name6", "avatarUrl"),
        UserRemoteDto("name7", "avatarUrl"),
        UserRemoteDto("name8", "avatarUrl"),
        UserRemoteDto("name9", "avatarUrl"),
        UserRemoteDto("name10", "avatarUrl"),
        UserRemoteDto("name11", "avatarUrl"),
        UserRemoteDto("name12", "avatarUrl"),
        UserRemoteDto("name13", "avatarUrl"),
        UserRemoteDto("name14", "avatarUrl"),
        UserRemoteDto("name15", "avatarUrl"),
        UserRemoteDto("name16", "avatarUrl"),
        UserRemoteDto("name17", "avatarUrl")
      )
    ).delay(500, TimeUnit.MILLISECONDS)
  }

  override fun retrieveUserRepositories(userLogin: String): Single<List<UserRepositoryRemoteDto>> {
    return Single.just(
      listOf(
        UserRepositoryRemoteDto("repo1"),
        UserRepositoryRemoteDto("repo2"),
        UserRepositoryRemoteDto("repo3"),
        UserRepositoryRemoteDto("repo4"),
        UserRepositoryRemoteDto("repo5")
      )
    ).delay(300, TimeUnit.MILLISECONDS)
  }

}