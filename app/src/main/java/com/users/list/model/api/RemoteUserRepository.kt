package com.users.list.model.api

import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import com.users.list.model.domain.UserEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.flatMapIterable
import javax.inject.Inject

class RemoteUserRepository @Inject constructor(
  private val userApi: UserApi
) : RemoteRepository {
  override fun retrieveUsers(): Single<List<UserEntity>> {
    return userApi.fetchUsers()
      .map { it.take(USERS_NUMBER) }
      .toObservable()
      .flatMapIterable()
      .flatMap(
        { fetchRepositories(it) },
        { user, repos -> UserEntity(user.login, user.avatarUrl, repos.map { it.name }) }
      )
      .toList()
  }

  private fun fetchRepositories(user: UserRemoteDto): Observable<List<UserRepositoryRemoteDto>> {
    return userApi.fetchUserRepository(user.login)
      .map { it.take(REPOSITORIES_NUMBER) }
      .toObservable()
  }

  companion object {
    private const val USERS_NUMBER = 30
    private const val REPOSITORIES_NUMBER = 3
  }
}