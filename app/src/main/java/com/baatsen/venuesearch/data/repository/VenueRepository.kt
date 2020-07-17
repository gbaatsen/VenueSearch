package com.baatsen.venuesearch.data.repository

import android.content.SharedPreferences
import android.provider.MediaStore.Video
import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.data.model.VenueMapper
import com.baatsen.venuesearch.data.service.FourSquareService
import com.baatsen.venuesearch.domain.model.Venue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import java.util.*


class VenueRepository(
    private val fourSquareService: FourSquareService,
    private val venueMapper: VenueMapper,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    val RADIUS = 10000
    val LIMIT = 10
    val VERSION_DATE = "20200701"

    fun getVenues(location: String): Single<List<Venue>> {
        return fourSquareService.getVenues(
            clientId = BuildConfig.CLIENT_ID,
            secretId = BuildConfig.SECRET_ID,
            near = location,
            radius = RADIUS,
            limit = LIMIT,
            versionDate = VERSION_DATE
        )
            .map { venueMapper.transform(it) }
            .doOnNext { store(location, it) }
            .onErrorReturn { getFromCache(location) }
            .singleOrError()
    }

    private fun getFromCache(location: String): List<Venue> {
        val itemType = object : TypeToken<List<Venue>>() {}.type
        val json = sharedPreferences.getString(location, null)
        return gson.fromJson<List<Venue>>(json, itemType)
    }

    private fun store(location: String, response: List<Venue>) {
        sharedPreferences.edit().putString(location, gson.toJson(response)).apply()
    }
}