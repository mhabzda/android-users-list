package com.mhabzda.userlist.domain

import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun retrieveUsers(): Flow<List<UserEntity>>
}
