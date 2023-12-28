package com.mhabzda.userlist.model.database

import com.mhabzda.userlist.model.domain.UserEntity
import io.reactivex.Single

interface LocalRepository {
    fun retrieveUsers(): Single<List<UserEntity>>
    fun insertUsers(users: List<UserEntity>)
    fun insertRepositories(userName: String, repositories: List<String>)
}
