package com.mhabzda.userlist.data.api

import com.mhabzda.userlist.data.api.mapper.UserRemoteMapper
import com.mhabzda.userlist.data.api.model.UserRepositoryRemoteDto
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class RemoteUserRepository @Inject constructor(
    private val userApi: UserApi,
    private val userRemoteMapper: UserRemoteMapper,
) {

    suspend fun retrieveUsers(): List<UserEntity> = coroutineScope {
        val users = userApi.fetchUsers().take(USERS_NUMBER)
        val repositoriesJobs = mutableMapOf<String, Deferred<List<UserRepositoryRemoteDto>>>()
        users.forEach { user ->
            repositoriesJobs[user.login] = async { userApi.fetchUserRepository(user.login).take(REPOSITORIES_NUMBER) }
        }
        return@coroutineScope users.map { userRemoteMapper.map(it, repositoriesJobs.getValue(it.login).await()) }
    }

    companion object {
        private const val USERS_NUMBER = 30
        private const val REPOSITORIES_NUMBER = 3
    }
}
