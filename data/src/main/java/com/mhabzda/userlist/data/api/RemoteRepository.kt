package com.mhabzda.userlist.data.api

import com.mhabzda.userlist.domain.model.UserEntity
import io.reactivex.Single

interface RemoteRepository {
    fun retrieveUsers(): Single<List<UserEntity>>
}
