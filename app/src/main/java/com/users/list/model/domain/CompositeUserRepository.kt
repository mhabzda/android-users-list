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
    override fun retrieveUsers(): Observable<List<UserEntity>> =
        Observable.mergeDelayError(
            localUserRepository.retrieveUsers().toObservable().map { UserData.Local(it) },
            remoteUserRepository.retrieveUsers().toObservable().map { UserData.Remote(it) }
        ).distinctUntilChanged { localData, remoteData ->
            localData.users.isContentTheSameAs(remoteData.users)
        }.saveRemoteData().map { it.users }

    override fun retrieveUsersLocally(): Single<List<UserEntity>> =
        localUserRepository.retrieveUsers()

    private fun Observable<UserData>.saveRemoteData(): Observable<UserData> =
        doOnNext { userData ->
            if (userData is UserData.Remote) {
                val users = userData.users
                localUserRepository.insertUsers(users)
                users.forEach { user -> localUserRepository.insertRepositories(user.name, user.repositories) }
            }
        }

    private fun <T> List<T>.isContentTheSameAs(otherList: List<T>): Boolean =
        containsAll(otherList) && otherList.containsAll(this)

    private sealed class UserData(val users: List<UserEntity>) {
        class Remote(users: List<UserEntity>) : UserData(users)
        class Local(users: List<UserEntity>) : UserData(users)
    }
}
