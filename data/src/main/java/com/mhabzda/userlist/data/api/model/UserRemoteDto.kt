package com.mhabzda.userlist.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class UserRemoteDto(
    @Json(name = "login")
    val login: String,

    @Json(name = "avatar_url")
    val avatarUrl: String,
)
