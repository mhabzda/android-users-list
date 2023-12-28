package com.mhabzda.userlist.domain.model

data class UserEntity(
    val name: String,
    val avatarUrl: String,
    val repositories: List<String>,
)
