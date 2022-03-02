package com.users.list.model.domain

import io.reactivex.Observable
import io.reactivex.Single

interface UserRepository {
    fun retrieveUsers(): Observable<List<UserEntity>>
    fun retrieveUsersLocally(): Single<List<UserEntity>>
}
