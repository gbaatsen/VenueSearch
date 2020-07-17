package com.baatsen.venuesearch

import io.reactivex.schedulers.Schedulers

object ImmediateSchedulerProvider : SchedulerProvider {

    override fun computation() = Schedulers.trampoline()

    override fun io() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()

}
