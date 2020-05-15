package com.users.list.model.domain

import com.users.list.model.api.RemoteRepository
import com.users.list.model.database.LocalUserRepository
import io.reactivex.Observable

class CompositeUserRepository(
  private val localUserRepository: LocalUserRepository,
  private val remoteUserRepository: RemoteRepository
) : UserRepository {
  override fun retrieveUsers(): Observable<List<UserEntity>> {
    return Observable.merge(
      localUserRepository.retrieveUsers().toObservable(),
      getRemoteUsers()
    ).distinctUntilChanged()
  }

  override fun retrieveUserRepositories(userName: String): Observable<List<String>> {
    return Observable.merge(
      localUserRepository.retrieveUserRepositories(userName).toObservable(),
      getRemoteRepositories(userName)
    ).distinctUntilChanged()
  }

  private fun getRemoteRepositories(userName: String): Observable<List<String>> {
    return remoteUserRepository.retrieveUserRepositories(userName).toObservable()
      .doOnNext { localUserRepository.insertRepositories(userName, it) }
  }

  private fun getRemoteUsers(): Observable<List<UserEntity>> {
    return remoteUserRepository.retrieveUsers().toObservable()
      .doOnNext { localUserRepository.insertUsers(it) }
  }
}