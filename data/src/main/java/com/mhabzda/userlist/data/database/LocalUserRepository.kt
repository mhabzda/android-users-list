package com.mhabzda.userlist.data.database

import com.mhabzda.userlist.data.database.dao.RepositoryDao
import com.mhabzda.userlist.data.database.dao.UserDao
import com.mhabzda.userlist.data.database.dtos.UserLocalDto
import com.mhabzda.userlist.data.database.dtos.UserRepositoryLocalDto
import com.mhabzda.userlist.data.database.mapper.UserLocalMapper
import com.mhabzda.userlist.domain.model.UserEntity
import io.reactivex.Single
import io.reactivex.rxkotlin.flatMapIterable
import javax.inject.Inject

class LocalUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val repositoryDao: RepositoryDao,
    private val userLocalMapper: UserLocalMapper,
) : LocalRepository {

    override fun retrieveUsers(): Single<List<UserEntity>> =
        userDao.getUsers().toObservable()
            .flatMapIterable()
            .flatMap(
                { user -> repositoryDao.getRepositories(user.login).toObservable() },
                { user, repos -> userLocalMapper.map(user, repos) }
            )
            .toList()

    override fun insertUsers(users: List<UserEntity>) =
        userDao.insert(
            *users
                .map { UserLocalDto(it.name, it.avatarUrl) }
                .toTypedArray()
        )

    override fun insertRepositories(userName: String, repositories: List<String>) =
        repositoryDao.insert(
            *repositories
                .map { UserRepositoryLocalDto(userName, it) }
                .toTypedArray()
        )
}
