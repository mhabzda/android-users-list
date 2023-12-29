package com.mhabzda.userlist.data

import com.mhabzda.userlist.data.api.RemoteUserRepository
import com.mhabzda.userlist.data.database.LocalUserRepository
import com.mhabzda.userlist.domain.UserRepository
import com.mhabzda.userlist.domain.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class CompositeUserRepository @Inject constructor(
    private val localUserRepository: LocalUserRepository,
    private val remoteUserRepository: RemoteUserRepository,
) : UserRepository {

    override suspend fun retrieveUsers(): Flow<List<UserEntity>> =
        flow {
            emit(UserData.Local(localUserRepository.retrieveUsers()))
            emit(UserData.Remote(remoteUserRepository.retrieveUsers()))
        }.distinctUntilChanged { localData, remoteData ->
            remoteData.users.isContentTheSameAs(localData.users)
        }.saveRemoteData().map { it.users }

    private fun Flow<UserData>.saveRemoteData(): Flow<UserData> =
        onEach { userData ->
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
