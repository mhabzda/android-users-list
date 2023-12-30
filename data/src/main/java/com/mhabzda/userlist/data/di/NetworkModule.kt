package com.mhabzda.userlist.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mhabzda.userlist.data.BuildConfig
import com.mhabzda.userlist.data.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {
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
    @Singleton
    fun provideUserApi(httpClient: OkHttpClient): UserApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(httpClient)
            .build()
            .create(UserApi::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.github.com"
    }
}
