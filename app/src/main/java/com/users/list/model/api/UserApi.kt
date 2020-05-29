package com.users.list.model.api

import com.users.list.model.api.dtos.UserRemoteDto
import com.users.list.model.api.dtos.UserRepositoryRemoteDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
  @GET("users")
  fun fetchUsers(): Single<List<UserRemoteDto>>

  @GET("users/{user_login}/repos")
  fun fetchUserRepository(@Path("user_login") userLogin: String): Single<List<UserRepositoryRemoteDto>>
}