package com.users.list.model.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRepositoryEntity(
  @Json(name = "name")
  val name: String
)
