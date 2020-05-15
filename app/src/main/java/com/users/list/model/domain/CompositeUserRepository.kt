package com.users.list.model.domain

import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.database.LocalUserRepository
import io.reactivex.Observable

class CompositeUserRepository(
  private val localUserRepository: LocalUserRepository,
  private val remoteUserRepository: RemoteUserRepository
) : UserRepository {
  override fun retrieveUsers(): Observable<List<UserEntity>> {
    return Observable.merge(
      localUserRepository.retrieveUsers().toObservable(),
      getRemoteUsers()
    )
  }

  override fun retrieveUserRepositories(userName: String): Observable<List<String>> {
    return Observable.merge(
      localUserRepository.retrieveUserRepositories(userName).toObservable(),
      getRemoteRepositories(userName)
    )
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