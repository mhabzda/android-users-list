package com.users.list.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.users.list.model.database.dao.RepositoryDao
import com.users.list.model.database.dao.UserDao
import com.users.list.model.database.dtos.UserLocalDto
import com.users.list.model.database.dtos.UserRepositoryLocalDto

@Database(
  entities = [UserLocalDto::class, UserRepositoryLocalDto::class],
  version = 1
)
abstract class UserDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
  abstract fun repositoryDao(): RepositoryDao

  companion object {
    private const val DATABASE_NAME = "user_database"

    @Volatile
    private var instance: UserDatabase? = null
    private val LOCK = Any()

    operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
      instance ?: buildDatabase(context).also {
        instance = it
      }
    }

    private fun buildDatabase(context: Context) = Room.databaseBuilder(
      context.applicationContext,
      UserDatabase::class.java,
      DATABASE_NAME
    ).build()
  }
}