package com.users.list.model.database

import android.app.Application
import com.users.list.model.database.dtos.UserLocalDto
import com.users.list.model.database.dtos.UserRepositoryLocalDto
import com.users.list.model.domain.UserEntity
import io.reactivex.Maybe
import io.reactivex.rxkotlin.flatMapIterable

class LocalUserRepository(
  private val application: Application
) {
  private val database by lazy { UserDatabase(application) }
  private val userDao by lazy { database.userDao() }
  private val repositoryDao by lazy { database.repositoryDao() }

  fun retrieveUsers(): Maybe<List<UserEntity>> {
    return userDao.getUsers().toObservable()
      .flatMapIterable()
      .flatMap(
        { user -> repositoryDao.getRepositories(user.login).toObservable() },
        { user, repos -> UserEntity(user.login, user.avatarUrl, repos.map { it.name }) }
      )
      .toList()
      .toMaybe()
  }

  fun insertUsers(users: List<UserEntity>) {
    userDao.insert(
      *users
        .map { UserLocalDto(it.name, it.avatarUrl) }
        .toTypedArray()
    )
  }

  fun insertRepositories(userName: String, repositories: List<String>) {
    repositoryDao.insert(
      *repositories
        .map { UserRepositoryLocalDto(userName, it) }
        .toTypedArray()
    )
  }
}