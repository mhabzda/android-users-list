package com.users.list.model.domain

import com.users.list.model.api.RemoteRepository
import com.users.list.model.database.LocalUserRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class CompositeUserRepository @Inject constructor(
  private val localUserRepository: LocalUserRepository,
  private val remoteUserRepository: RemoteRepository
) : UserRepository {
  override fun retrieveUsers(): Observable<List<UserEntity>> {
    return Observable.merge(
      localUserRepository.retrieveUsers().toObservable(),
      getRemoteUsers()
    ).distinctUntilChanged()
  }

  override fun retrieveUsersLocally(): Maybe<List<UserEntity>> {
    return localUserRepository.retrieveUsers()
  }

  private fun getRemoteUsers(): Observable<List<UserEntity>> {
    return remoteUserRepository.retrieveUsers().toObservable()
      .doOnNext { users ->
        localUserRepository.insertUsers(users)
        users.forEach { user ->
          localUserRepository.insertRepositories(user.name, user.repositories)
        }
      }
  }
}