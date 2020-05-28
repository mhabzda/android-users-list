package com.users.list.model.api

import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import com.users.list.model.api.mapper.UserRemoteMapper
import com.users.list.model.domain.UserEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.flatMapIterable
import javax.inject.Inject

typealias RepositoriesListResult = Result<List<UserRepositoryRemoteDto>>

class RemoteUserRepository @Inject constructor(
  private val userApi: UserApi,
  private val userRemoteMapper: UserRemoteMapper
) : RemoteRepository {
  override fun retrieveUsers(): Single<List<UserEntity>> {
    return userApi.fetchUsers()
      .map { it.take(USERS_NUMBER) }
      .toObservable()
      .flatMapIterable()
      .flatMap(this::fetchRepositories, mergeUserWithRepositories())
      .toList()
      .map { userList -> userList.map { user -> user.getOrThrow() } }
  }

  private fun fetchRepositories(user: UserRemoteDto): Observable<RepositoriesListResult> {
    return userApi.fetchUserRepository(user.login)
      .map { Result.success(it.take(REPOSITORIES_NUMBER)) }
      .onErrorReturn { Result.failure(it) }
      .toObservable()
  }

  private fun mergeUserWithRepositories(): (UserRemoteDto, RepositoriesListResult) -> Result<UserEntity> {
    return { user, repositoriesResult ->
      val exception = repositoriesResult.exceptionOrNull()
      if (exception == null) {
        Result.success(userRemoteMapper.map(user, repositoriesResult.getOrThrow()))
      } else {
        Result.failure(exception)
      }
    }
  }

  companion object {
    private const val USERS_NUMBER = 30
    private const val REPOSITORIES_NUMBER = 3
  }
}