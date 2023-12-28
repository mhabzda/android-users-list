package com.mhabzda.userlist.model.domain

import io.reactivex.Observable

interface UserRepository {
    fun retrieveUsers(): Observable<List<UserEntity>>
}
