package com.mhabzda.userlist.data.api.mapper

import com.mhabzda.userlist.data.api.dtos.UserRemoteDto
import com.mhabzda.userlist.data.api.dtos.UserRepositoryRemoteDto
import com.mhabzda.userlist.domain.model.UserEntity
import javax.inject.Inject

class UserRemoteMapper @Inject constructor() {
    fun map(user: UserRemoteDto, repositoriesList: List<UserRepositoryRemoteDto>): UserEntity =
        UserEntity(
            name = user.login,
            avatarUrl = user.avatarUrl,
            repositories = repositoriesList.map { it.name }
        )
}
