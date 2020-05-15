package com.users.list.model

import com.users.list.model.api.entities.UserEntity
import com.users.list.model.api.entities.UserRepositoryEntity
import io.reactivex.Single

interface UserRepository {
  fun retrieveUsers(): Single<List<UserEntity>>
  fun retrieveUserRepository(userLogin: String): Single<UserRepositoryEntity>
}