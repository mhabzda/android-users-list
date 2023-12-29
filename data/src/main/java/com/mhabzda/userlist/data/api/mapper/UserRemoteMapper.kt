package com.mhabzda.userlist.data.api.mapper

import com.mhabzda.userlist.data.api.model.UserRemoteDto
import com.mhabzda.userlist.data.api.model.UserRepositoryRemoteDto
import com.mhabzda.userlist.domain.model.UserEntity
import javax.inject.Inject

internal class UserRemoteMapper @Inject constructor() {
    fun map(user: UserRemoteDto, repositoriesList: List<UserRepositoryRemoteDto>): UserEntity =
        UserEntity(
            name = user.login,
            avatarUrl = user.avatarUrl,
            repositories = repositoriesList.map { it.name },
        )
}
