package com.users.list.model.database

import com.users.list.model.domain.UserEntity
import io.reactivex.Single

interface LocalRepository {
  fun retrieveUsers(): Single<List<UserEntity>>
  fun insertUsers(users: List<UserEntity>)
  fun insertRepositories(userName: String, repositories: List<String>)
}