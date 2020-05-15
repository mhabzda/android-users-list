package com.users.list.model.domain

data class UserEntity(
  val name: String,
  val avatarUrl: String,
  val repositoriesNames: List<String>
)