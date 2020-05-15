package com.users.list.model.api.fake

import com.users.list.model.api.RemoteRepository
import com.users.list.model.domain.UserEntity
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class FakeUserRepository : RemoteRepository {
  override fun retrieveUsers(): Single<List<UserEntity>> {
    return Single.just(
      listOf(
        UserEntity("name1", "avatarUrl"),
        UserEntity("name2", "avatarUrl"),
        UserEntity("name3", "avatarUrl"),
        UserEntity("name4", "avatarUrl"),
        UserEntity("name5", "avatarUrl"),
        UserEntity("name6", "avatarUrl"),
        UserEntity("name7", "avatarUrl"),
        UserEntity("name8", "avatarUrl"),
        UserEntity("name9", "avatarUrl"),
        UserEntity("name10", "avatarUrl"),
        UserEntity("name11", "avatarUrl"),
        UserEntity("name12", "avatarUrl"),
        UserEntity("name13", "avatarUrl"),
        UserEntity("name14", "avatarUrl"),
        UserEntity("name15", "avatarUrl"),
        UserEntity("name16", "avatarUrl"),
        UserEntity("name17", "avatarUrl")
      )
    ).delay(500, TimeUnit.MILLISECONDS)
  }

  override fun retrieveUserRepositories(userName: String): Single<List<String>> {
    return Single.just(
      listOf(
        "repo1",
        "repo2",
        "repo3"
      )
    ).delay(300, TimeUnit.MILLISECONDS)
  }

}