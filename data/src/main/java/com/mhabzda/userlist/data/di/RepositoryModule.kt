package com.mhabzda.userlist.data.di

import com.mhabzda.userlist.data.CompositeUserRepository
import com.mhabzda.userlist.data.api.RemoteRepository
import com.mhabzda.userlist.data.api.RemoteUserRepository
import com.mhabzda.userlist.data.database.LocalRepository
import com.mhabzda.userlist.data.database.LocalUserRepository
import com.mhabzda.userlist.domain.UserRepository
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
