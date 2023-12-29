package com.mhabzda.userlist.data.database

import com.mhabzda.userlist.data.database.dao.RepositoryDao
import com.mhabzda.userlist.data.database.dao.UserDao
import com.mhabzda.userlist.data.database.mapper.UserLocalMapper
import com.mhabzda.userlist.data.database.model.UserLocalDto
import com.mhabzda.userlist.data.database.model.UserRepositoryLocalDto
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

internal class LocalUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val repositoryDao: RepositoryDao,
    private val userLocalMapper: UserLocalMapper,
) {

    suspend fun retrieveUsers(): List<UserEntity> = coroutineScope {
        val users = userDao.getUsers()
        val repositoriesJobs = mutableMapOf<String, Deferred<List<UserRepositoryLocalDto>>>()
        users.forEach { user ->
            repositoriesJobs[user.login] = async { repositoryDao.getRepositories(user.login) }
        }
        return@coroutineScope users.map { userLocalMapper.map(it, repositoriesJobs.getValue(it.login).await()) }
    }

    suspend fun insertUsers(users: List<UserEntity>) =
        userDao.insert(
            *users
                .map { UserLocalDto(it.name, it.avatarUrl) }
                .toTypedArray(),
        )

    suspend fun insertRepositories(userName: String, repositories: List<String>) =
        repositoryDao.insert(
            *repositories
                .map { UserRepositoryLocalDto(userName, it) }
                .toTypedArray(),
        )
}
