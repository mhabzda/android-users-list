package com.users.list.model.domain

import io.reactivex.Observable

interface UserRepository {
  fun retrieveUsers(): Observable<List<UserEntity>>
  fun retrieveUserRepositories(userName: String): Observable<List<String>>
}