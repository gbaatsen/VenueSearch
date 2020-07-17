package com.baatsen.venuesearch

import android.app.Application
import com.baatsen.venuesearch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VenueSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@VenueSearchApplication)
            // use modules
            modules(appModule)
        }

    }
}