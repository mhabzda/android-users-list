package com.mhabzda.userlist.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UserRepositoryRemoteDto(
    @SerialName(value = "name")
    val name: String,
)
