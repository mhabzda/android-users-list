package com.users.list.ui.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {
  fun io(): Scheduler
  fun ui(): Scheduler
}