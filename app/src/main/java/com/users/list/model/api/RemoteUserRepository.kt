package com.users.list.model.api

import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import com.users.list.model.domain.UserEntity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.flatMapIterable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteUserRepository : RemoteRepository {
  private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com")
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

  private val userApi = retrofit.create(UserApi::class.java)

  override fun retrieveUsers(): Single<List<UserEntity>> {
    return userApi.fetchUsers().map { it.take(USERS_NUMBER) }.toObservable()
      .flatMapIterable()
      .flatMap(
        { fetchRepositories(it) },
        { users, repos -> UserEntity(users.login, users.avatarUrl, repos.map { it.name }) }
      )
      .toList()
  }

  private fun fetchRepositories(user: UserRemoteDto): Observable<List<UserRepositoryRemoteDto>> {
    return userApi.fetchUserRepository(user.login)
      .map { it.take(REPOSITORIES_NUMBER) }
      .onErrorReturn { emptyList() }
      .toObservable()
  }

  companion object {
    private const val USERS_NUMBER = 30
    private const val REPOSITORIES_NUMBER = 3
  }
}