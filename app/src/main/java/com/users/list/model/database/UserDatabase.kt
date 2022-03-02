package com.users.list.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.users.list.model.database.dao.RepositoryDao
import com.users.list.model.database.dao.UserDao
import com.users.list.model.database.dtos.UserLocalDto
import com.users.list.model.database.dtos.UserRepositoryLocalDto

@Database(
    entities = [UserLocalDto::class, UserRepositoryLocalDto::class],
    version = 2,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
}
