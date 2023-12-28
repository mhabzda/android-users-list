package com.mhabzda.userlist.model.domain

data class UserEntity(
    val name: String,
    val avatarUrl: String,
    val repositories: List<String>,
)
