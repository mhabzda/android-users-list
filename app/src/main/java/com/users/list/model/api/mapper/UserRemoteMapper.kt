package com.users.list.model.api.mapper

import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import com.users.list.model.domain.UserEntity
import javax.inject.Inject

class UserRemoteMapper @Inject constructor() {
  fun map(user: UserRemoteDto, repositoriesList: List<UserRepositoryRemoteDto>): UserEntity {
    return UserEntity(
      name = user.login,
      avatarUrl = user.avatarUrl,
      repositories = repositoriesList.map { it.name }
    )
  }
}