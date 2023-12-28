package com.users.list.di

import android.content.Context
import androidx.room.Room
import com.users.list.model.database.UserDatabase
import com.users.list.model.database.dao.RepositoryDao
import com.users.list.model.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context): UserDatabase =
        Room.databaseBuilder(
            context = applicationContext,
            klass = UserDatabase::class.java,
            name = DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao =
        userDatabase.userDao()

    @Singleton
    @Provides
    fun provideRepositoryDao(userDatabase: UserDatabase): RepositoryDao =
        userDatabase.repositoryDao()

    companion object {
        private const val DATABASE_NAME = "user_database"
    }
}
