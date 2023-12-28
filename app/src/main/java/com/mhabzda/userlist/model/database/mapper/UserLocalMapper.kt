package com.mhabzda.userlist.model.database.mapper

import com.mhabzda.userlist.model.database.dtos.UserLocalDto
import com.mhabzda.userlist.model.database.dtos.UserRepositoryLocalDto
import com.mhabzda.userlist.model.domain.UserEntity
import javax.inject.Inject

class UserLocalMapper @Inject constructor() {
    fun map(user: UserLocalDto, repositories: List<UserRepositoryLocalDto>): UserEntity =
        UserEntity(
            name = user.login,
            avatarUrl = user.avatarUrl,
            repositories = repositories.map { it.name }
        )
}
