package com.mhabzda.userlist.domain

import com.mhabzda.userlist.domain.model.UserEntity
import io.reactivex.Observable

interface UserRepository {
    fun retrieveUsers(): Observable<List<UserEntity>>
}
