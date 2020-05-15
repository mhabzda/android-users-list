package com.users.list.model

import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import io.reactivex.Single

interface UserRepository {
  fun retrieveUsers(): Single<List<UserRemoteDto>>
  fun retrieveUserRepositories(userLogin: String): Single<List<UserRepositoryRemoteDto>>
}