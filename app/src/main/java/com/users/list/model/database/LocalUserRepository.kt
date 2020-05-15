package com.users.list.model.database

import android.app.Application
import com.users.list.model.database.dtos.UserLocalDto
import com.users.list.model.database.dtos.UserRepositoryLocalDto
import com.users.list.model.domain.UserEntity
import io.reactivex.Maybe

class LocalUserRepository(
  private val application: Application
) {
  private val database by lazy { UserDatabase(application) }
  private val userDao by lazy { database.userDao() }
  private val repositoryDao by lazy { database.repositoryDao() }

  fun retrieveUsers(): Maybe<List<UserEntity>> {
    return userDao.getUsers()
      .map { users -> users.map { UserEntity(it.login, it.avatarUrl) } }
  }

  fun retrieveUserRepositories(userName: String): Maybe<List<String>> {
    return repositoryDao.getRepositories(userName)
      .map { repos -> repos.map { it.name } }
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