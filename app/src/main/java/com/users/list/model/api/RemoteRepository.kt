package com.users.list.model.api

import com.users.list.model.domain.UserEntity
import io.reactivex.Single

interface RemoteRepository {
    fun retrieveUsers(): Single<List<UserEntity>>
}
