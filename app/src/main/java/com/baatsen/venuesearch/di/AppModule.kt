package com.baatsen.venuesearch.di

import android.content.Context
import android.net.ConnectivityManager
import com.baatsen.venuesearch.AndroidSchedulerProvider
import com.baatsen.venuesearch.CacheInterceptor
import com.baatsen.venuesearch.SchedulerProvider
import com.baatsen.venuesearch.data.model.VenueDetailsMapper
import com.baatsen.venuesearch.data.model.VenueMapper
import com.baatsen.venuesearch.data.repository.VenueDetailsRepository
import com.baatsen.venuesearch.data.repository.VenueRepository
import com.baatsen.venuesearch.data.service.FourSquareApiConfig
import com.baatsen.venuesearch.domain.interactor.GetVenueDetailsService
import com.baatsen.venuesearch.domain.interactor.GetVenuesService
import com.baatsen.venuesearch.presentation.venuedetails.VenueDetailsViewModel
import com.baatsen.venuesearch.presentation.venues.VenueViewModel
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

val appModule = module {
    //ViewModels
    viewModel { VenueViewModel(get(), get()) }
    viewModel { VenueDetailsViewModel(get(), get()) }

    //Repos
    factory { VenueRepository(get(), get()) }
    factory { VenueDetailsRepository(get(), get()) }

    //Mappers
    factory { VenueMapper() }
    factory { VenueDetailsMapper() }

    //Network
    factory { GetVenuesService(get()) }
    factory { GetVenueDetailsService(get()) }
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single {
        val cacheDir = File(androidContext().cacheDir, "http")
        FourSquareApiConfig(get(), get(), cacheDir)
    }
    single { CacheInterceptor(get()) }
    single { HttpLoggingInterceptor() }

    //Scheduler provider
    single<SchedulerProvider> { AndroidSchedulerProvider }
}