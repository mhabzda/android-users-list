package com.mhabzda.userlist.data.api

import com.mhabzda.userlist.data.api.dtos.UserRemoteDto
import com.mhabzda.userlist.data.api.dtos.UserRepositoryRemoteDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users")
    fun fetchUsers(): Single<List<UserRemoteDto>>

    @GET("users/{user_login}/repos")
    fun fetchUserRepository(@Path("user_login") userLogin: String): Single<List<UserRepositoryRemoteDto>>
}
