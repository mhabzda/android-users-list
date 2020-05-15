package com.users.list.model.domain

import io.reactivex.Maybe
import io.reactivex.Observable

interface UserRepository {
  fun retrieveUsers(): Observable<List<UserEntity>>
  fun retrieveUsersLocally(): Maybe<List<UserEntity>>
}