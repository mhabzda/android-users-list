package com.mhabzda.userlist.model.api.dtos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRemoteDto(
    @Json(name = "login")
    val login: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,
)
