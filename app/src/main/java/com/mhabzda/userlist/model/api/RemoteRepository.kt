package com.mhabzda.userlist.model.api

import com.mhabzda.userlist.model.domain.UserEntity
import io.reactivex.Single

interface RemoteRepository {
    fun retrieveUsers(): Single<List<UserEntity>>
}
