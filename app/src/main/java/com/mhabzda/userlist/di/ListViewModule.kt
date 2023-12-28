package com.mhabzda.userlist.di

import com.mhabzda.userlist.ui.ListContract
import com.mhabzda.userlist.ui.ListPresenter
import com.mhabzda.userlist.ui.schedulers.AndroidSchedulerProvider
import com.mhabzda.userlist.ui.schedulers.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ListViewModule {
    @Binds
    abstract fun provideListPresenter(listPresenter: ListPresenter): ListContract.Presenter

    @Binds
    abstract fun provideSchedulerProvider(androidSchedulerProvider: AndroidSchedulerProvider): SchedulerProvider
}
