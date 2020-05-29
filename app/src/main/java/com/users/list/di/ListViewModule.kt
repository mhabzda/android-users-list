package com.users.list.di

import com.users.list.ui.ListActivity
import com.users.list.ui.ListContract
import com.users.list.ui.ListPresenter
import com.users.list.ui.schedulers.AndroidSchedulerProvider
import com.users.list.ui.schedulers.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ListViewModule {
  @Binds
  abstract fun provideListPresenter(listPresenter: ListPresenter): ListContract.Presenter

  @Binds
  abstract fun provideListView(listActivity: ListActivity): ListContract.View

  @Binds
  abstract fun provideSchedulerProvider(androidSchedulerProvider: AndroidSchedulerProvider): SchedulerProvider
}