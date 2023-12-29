package com.mhabzda.userlist.data.api

import com.mhabzda.userlist.data.api.model.UserRemoteDto
import com.mhabzda.userlist.data.api.model.UserRepositoryRemoteDto
import retrofit2.http.GET
import retrofit2.http.Path

internal interface UserApi {
    @GET("users")
    suspend fun fetchUsers(): List<UserRemoteDto>

    @GET("users/{user_login}/repos")
    suspend fun fetchUserRepository(@Path("user_login") userLogin: String): List<UserRepositoryRemoteDto>
}
