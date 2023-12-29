package com.mhabzda.userlist.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class UserRepositoryRemoteDto(
    @Json(name = "name")
    val name: String,
)
