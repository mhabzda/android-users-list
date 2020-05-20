package com.users.list.di

import com.users.list.model.api.RemoteRepository
import com.users.list.model.api.RemoteUserRepository
import com.users.list.model.api.UserApi
import com.users.list.model.domain.CompositeUserRepository
import com.users.list.model.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
abstract class RepositoryModule {
  @Binds
  abstract fun provideRemoteRepository(remoteUserRepository: RemoteUserRepository): RemoteRepository

  @Binds
  abstract fun provideUserRepository(compositeUserRepository: CompositeUserRepository): UserRepository

  companion object {
    private const val BASE_URL = "https://api.github.com"

    @JvmStatic
    @Provides
    fun provideUserApi(): UserApi {
      return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(UserApi::class.java)
    }
  }
}