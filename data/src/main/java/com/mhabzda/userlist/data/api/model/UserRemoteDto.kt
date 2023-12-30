package com.mhabzda.userlist.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserRemoteDto(
    @SerialName(value = "login")
    val login: String,

    @SerialName(value = "avatar_url")
    val avatarUrl: String,
)
