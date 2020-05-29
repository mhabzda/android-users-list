package com.users.list.di

import com.users.BuildConfig
import com.users.list.model.api.UserApi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {
  @Provides
  fun provideHttpClient(): OkHttpClient {
    val httpClientBuilder = OkHttpClient.Builder()

    val accessToken = BuildConfig.GITHUB_TOKEN
    if (accessToken.isNotBlank()) {
      httpClientBuilder.addInterceptor { chain ->
        val request = chain.request().newBuilder()
          .header("Authorization", "token $accessToken")
          .build()

        chain.proceed(request)
      }
    }

    return httpClientBuilder.build()
  }

  @Provides
  fun provideUserApi(httpClient: OkHttpClient): UserApi {
    return Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
      .addConverterFactory(MoshiConverterFactory.create())
      .client(httpClient)
      .build()
      .create(UserApi::class.java)
  }

  companion object {
    private const val BASE_URL = "https://api.github.com"
  }
}