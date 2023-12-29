package com.mhabzda.userlist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mhabzda.userlist.data.database.dao.RepositoryDao
import com.mhabzda.userlist.data.database.dao.UserDao
import com.mhabzda.userlist.data.database.model.UserLocalDto
import com.mhabzda.userlist.data.database.model.UserRepositoryLocalDto

@Database(
    entities = [UserLocalDto::class, UserRepositoryLocalDto::class],
    version = 2,
    exportSchema = false
)
internal abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryDao(): RepositoryDao
}
