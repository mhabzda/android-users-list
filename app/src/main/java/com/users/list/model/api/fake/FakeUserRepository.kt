package com.users.list.model.api.fake

import com.users.list.model.api.RemoteRepository
import com.users.list.model.domain.UserEntity
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FakeUserRepository @Inject constructor() : RemoteRepository {
    override fun retrieveUsers(): Single<List<UserEntity>> =
        Single.just(
            listOf(
                UserEntity("name1", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name2", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name3", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name4", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name5", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name6", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name7", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name8", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name9", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name10", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name11", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name12", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name13", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name14", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name15", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name16", "avatarUrl", listOf("repo1", "repo2", "repo3")),
                UserEntity("name17", "avatarUrl", listOf("repo1", "repo2", "repo3"))
            )
        ).delay(500, TimeUnit.MILLISECONDS)
}
