package com.users.list

import com.users.list.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App : DaggerApplication() {
  override fun applicationInjector(): AndroidInjector<DaggerApplication> {
    return DaggerApplicationComponent.factory().create(this)
  }
}