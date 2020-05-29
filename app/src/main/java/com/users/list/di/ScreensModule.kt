package com.users.list.di

import com.users.list.ui.ListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ScreensModule {
  @ContributesAndroidInjector(modules = [ListViewModule::class])
  abstract fun bindListActivity(): ListActivity
}