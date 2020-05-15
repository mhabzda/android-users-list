package com.users.list.ui.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidSchedulerProvider : SchedulerProvider {
  override fun io(): Scheduler = Schedulers.io()
  override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}