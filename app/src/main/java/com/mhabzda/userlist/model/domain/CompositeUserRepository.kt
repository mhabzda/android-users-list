package com.mhabzda.userlist.model.domain

import com.mhabzda.userlist.model.api.RemoteRepository
import com.mhabzda.userlist.model.database.LocalRepository
import io.reactivex.Observable
import javax.inject.Inject

class CompositeUserRepository @Inject constructor(
    private val localUserRepository: LocalRepository,
    private val remoteUserRepository: RemoteRepository,
) : UserRepository {
    override fun retrieveUsers(): Observable<List<UserEntity>> =
        Observable.mergeDelayError(
            localUserRepository.retrieveUsers().toObservable().map { UserData.Local(it) },
            remoteUserRepository.retrieveUsers().toObservable().map { UserData.Remote(it) }
        ).distinctUntilChanged { localData, remoteData ->
            localData.users.isContentTheSameAs(remoteData.users)
        }.saveRemoteData().map { it.users }

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
