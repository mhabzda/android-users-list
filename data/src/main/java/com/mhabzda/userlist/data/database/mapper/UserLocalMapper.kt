package com.mhabzda.userlist.data.database.mapper

import com.mhabzda.userlist.data.database.dtos.UserLocalDto
import com.mhabzda.userlist.data.database.dtos.UserRepositoryLocalDto
import com.mhabzda.userlist.domain.model.UserEntity
import javax.inject.Inject

class UserLocalMapper @Inject constructor() {
    fun map(user: UserLocalDto, repositories: List<UserRepositoryLocalDto>): UserEntity =
        UserEntity(
            name = user.login,
            avatarUrl = user.avatarUrl,
            repositories = repositories.map { it.name }
        )
}
