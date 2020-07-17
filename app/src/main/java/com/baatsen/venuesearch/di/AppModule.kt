package com.baatsen.venuesearch.di

import android.content.SharedPreferences
import com.baatsen.venuesearch.AndroidSchedulerProvider
import com.baatsen.venuesearch.SchedulerProvider
import com.baatsen.venuesearch.data.model.VenueDetailsMapper
import com.baatsen.venuesearch.data.model.VenueMapper
import com.baatsen.venuesearch.data.repository.VenueDetailsRepository
import com.baatsen.venuesearch.data.repository.VenueRepository
import com.baatsen.venuesearch.data.service.FourSquareService
import com.baatsen.venuesearch.domain.interactor.GetVenueDetailsService
import com.baatsen.venuesearch.domain.interactor.GetVenuesService
import com.baatsen.venuesearch.presentation.venuedetails.VenueDetailsViewModel
import com.baatsen.venuesearch.presentation.venues.VenueViewModel
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.defaultSharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.foursquare.com/"
private val forSquareService = createForSquareService()

fun createForSquareService(): FourSquareService {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()


    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(client)
        .build()
        .create(FourSquareService::class.java)
}

val appModule = module {
    single<SharedPreferences> { (androidApplication().defaultSharedPreferences) }

    //ViewModels
    viewModel { VenueViewModel(get(), get()) }
    viewModel { VenueDetailsViewModel(get(), get()) }

    //Repos
    factory { VenueRepository(forSquareService, get(), get(), get()) }
    factory { VenueDetailsRepository(forSquareService, get(), get(), get()) }

    //other stuff
    factory { GetVenuesService(get()) }
    factory { GetVenueDetailsService(get()) }
    factory { VenueMapper() }
    factory { VenueDetailsMapper() }
    single { Gson() }
    single<SchedulerProvider> { AndroidSchedulerProvider }

}