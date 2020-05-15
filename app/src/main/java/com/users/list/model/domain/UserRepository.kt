package com.users.list.model.domain

import io.reactivex.Observable
import io.reactivex.Single

typealias User = Pair<UserEntity, List<String>>

interface UserRepository {
  fun retrieveUsers(): Observable<List<UserEntity>>
  fun retrieveUserRepositories(userName: String): Observable<List<String>>
  fun retrieveUsersLocally(): Single<List<User>>
}