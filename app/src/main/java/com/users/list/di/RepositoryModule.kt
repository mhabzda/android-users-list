package com.users.list.di

import com.users.list.model.api.RemoteRepository
import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.domain.CompositeUserRepository
import com.users.list.model.domain.UserRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
  @Binds
  abstract fun provideRemoteRepository(remoteUserRepository: RemoteUserRepository): RemoteRepository

  @Binds
  abstract fun provideUserRepository(compositeUserRepository: CompositeUserRepository): UserRepository
}