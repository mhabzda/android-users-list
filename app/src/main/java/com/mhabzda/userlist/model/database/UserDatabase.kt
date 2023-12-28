package com.mhabzda.userlist.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mhabzda.userlist.model.database.dao.RepositoryDao
import com.mhabzda.userlist.model.database.dao.UserDao
import com.mhabzda.userlist.model.database.dtos.UserLocalDto
import com.mhabzda.userlist.model.database.dtos.UserRepositoryLocalDto

@Database(
    entities = [UserLocalDto::class, UserRepositoryLocalDto::class],
    version = 2,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
}
