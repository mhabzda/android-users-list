package com.mhabzda.userlist.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
internal data class UserLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String,
)
