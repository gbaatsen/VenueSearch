package com.baatsen.venuesearch.data.service

import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.CacheInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.foursquare.com/"

class FourSquareApiConfig(
    private val cacheInterceptor: CacheInterceptor,
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val cacheDir: File

) {
    fun get(): FourSquareApi {
        val cacheSize = (5 * 1024 * 1024).toLong() //5MB
        val cache = Cache(cacheDir, cacheSize)

        if (BuildConfig.DEBUG) httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(cacheInterceptor)
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
            .create(FourSquareApi::class.java)
    }
}