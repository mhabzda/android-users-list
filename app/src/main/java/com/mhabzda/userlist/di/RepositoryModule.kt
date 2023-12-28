package com.mhabzda.userlist.di

import com.mhabzda.userlist.model.api.RemoteRepository
import com.mhabzda.userlist.model.api.RemoteUserRepository
import com.mhabzda.userlist.model.database.LocalRepository
import com.mhabzda.userlist.model.database.LocalUserRepository
import com.mhabzda.userlist.model.domain.CompositeUserRepository
import com.mhabzda.userlist.model.domain.UserRepository
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
