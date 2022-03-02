package com.users.list.di

import androidx.room.Room
import com.users.list.model.database.UserDatabase
import com.users.list.model.database.dao.RepositoryDao
import com.users.list.model.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(application: DaggerApplication): UserDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            UserDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideRepositoryDao(userDatabase: UserDatabase): RepositoryDao {
        return userDatabase.repositoryDao()
    }

    companion object {
        private const val DATABASE_NAME = "user_database"
    }
}
