package com.users.list.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidInjectionModule::class,
    ScreensModule::class,
    DatabaseModule::class
  ]
)
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
  @Component.Factory
  interface Factory : AndroidInjector.Factory<DaggerApplication>
}