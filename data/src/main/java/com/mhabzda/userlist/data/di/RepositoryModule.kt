package com.mhabzda.userlist.data.di

import com.mhabzda.userlist.data.CompositeUserRepository
import com.mhabzda.userlist.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    abstract fun provideUserRepository(compositeUserRepository: CompositeUserRepository): UserRepository
}
