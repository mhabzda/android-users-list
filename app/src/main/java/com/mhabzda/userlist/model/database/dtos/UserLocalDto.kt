package com.mhabzda.userlist.model.database.dtos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)
