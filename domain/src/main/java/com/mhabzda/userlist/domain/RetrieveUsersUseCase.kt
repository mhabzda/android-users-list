package com.mhabzda.userlist.domain

import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrieveUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(): Flow<List<UserEntity>> =
        userRepository.retrieveUsers()
}
