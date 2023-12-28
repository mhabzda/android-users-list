package com.mhabzda.userlist.data.database

import com.mhabzda.userlist.domain.model.UserEntity
import io.reactivex.Single

interface LocalRepository {
    fun retrieveUsers(): Single<List<UserEntity>>
    fun insertUsers(users: List<UserEntity>)
    fun insertRepositories(userName: String, repositories: List<String>)
}
