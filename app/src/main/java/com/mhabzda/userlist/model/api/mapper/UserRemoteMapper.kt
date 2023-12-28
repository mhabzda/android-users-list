package com.mhabzda.userlist.model.api.mapper

import com.mhabzda.userlist.model.api.dtos.UserRemoteDto
import com.mhabzda.userlist.model.api.dtos.UserRepositoryRemoteDto
import com.mhabzda.userlist.model.domain.UserEntity
import javax.inject.Inject

class UserRemoteMapper @Inject constructor() {
    fun map(user: UserRemoteDto, repositoriesList: List<UserRepositoryRemoteDto>): UserEntity =
        UserEntity(
            name = user.login,
            avatarUrl = user.avatarUrl,
            repositories = repositoriesList.map { it.name }
        )
}
