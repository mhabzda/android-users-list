package com.users.list.di

import com.users.list.model.api.RemoteRepository
import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.database.LocalRepository
import com.users.list.model.database.LocalUserRepository
import com.users.list.model.domain.CompositeUserRepository
import com.users.list.model.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideLocalRepository(localUserRepository: LocalUserRepository): LocalRepository

    @Binds
    abstract fun provideRemoteRepository(remoteUserRepository: RemoteUserRepository): RemoteRepository

    @Binds
    abstract fun provideUserRepository(compositeUserRepository: CompositeUserRepository): UserRepository
}
