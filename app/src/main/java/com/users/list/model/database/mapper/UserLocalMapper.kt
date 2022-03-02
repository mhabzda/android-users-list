package com.users.list.model.database.mapper

import com.users.list.model.database.dtos.UserLocalDto
import com.users.list.model.database.dtos.UserRepositoryLocalDto
import com.users.list.model.domain.UserEntity
import javax.inject.Inject

class UserLocalMapper @Inject constructor() {
    fun map(user: UserLocalDto, repositories: List<UserRepositoryLocalDto>): UserEntity {
        return UserEntity(
            name = user.login,
            avatarUrl = user.avatarUrl,
            repositories = repositories.map { it.name }
        )
    }
}
