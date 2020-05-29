package com.users.list.model.domain

import com.users.list.model.api.RemoteRepository
import com.users.list.model.database.LocalRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class CompositeUserRepository @Inject constructor(
  private val localUserRepository: LocalRepository,
  private val remoteUserRepository: RemoteRepository
) : UserRepository {
  override fun retrieveUsers(): Observable<List<UserEntity>> {
    return Observable.mergeDelayError(
      localUserRepository.retrieveUsers().toObservable().map { UserData.Local(it) },
      remoteUserRepository.retrieveUsers().toObservable().map { UserData.Remote(it) }
    ).distinctUntilChanged { localData, remoteData ->
      localData.users.containsAll(remoteData.users) && remoteData.users.containsAll(localData.users)
    }.saveRemoteData().map { it.users }
  }

  override fun retrieveUsersLocally(): Single<List<UserEntity>> {
    return localUserRepository.retrieveUsers()
  }

  private fun Observable<UserData>.saveRemoteData(): Observable<UserData> {
    return doOnNext { userData ->
      if (userData is UserData.Remote) {
        val users = userData.users
        localUserRepository.insertUsers(users)
        users.forEach { user -> localUserRepository.insertRepositories(user.name, user.repositories) }
      }
    }
  }

  private sealed class UserData(val users: List<UserEntity>) {
    class Remote(users: List<UserEntity>) : UserData(users)
    class Local(users: List<UserEntity>) : UserData(users)
  }
}