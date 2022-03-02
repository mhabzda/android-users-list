package com.users.list.ui

import com.users.list.ui.schedulers.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider : SchedulerProvider {
    private val testScheduler = TestScheduler()

    override fun io(): Scheduler {
        return testScheduler
    }

    override fun ui(): Scheduler {
        return testScheduler
    }

    fun triggerActions() {
        testScheduler.triggerActions()
    }
}
