package com.users.list.model.api

import com.users.list.model.api.entities.UserEntity
import com.users.list.model.api.entities.UserRepositoryEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
  @GET("users")
  fun fetchUsers(): Single<List<UserEntity>>

  @GET("{userLogin}/repos")
  fun fetchUserRepository(@Path("userLogin") userLogin: String): Single<UserRepositoryEntity>
}