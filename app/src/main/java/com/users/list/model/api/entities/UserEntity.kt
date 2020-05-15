package com.users.list.model.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserEntity(
  @Json(name = "login")
  val login: String,

  @Json(name = "avatar_url")
  val avatarUrl: String
)