package com.baatsen.venuesearch.data.repository

import android.content.SharedPreferences
import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.data.model.VenueDetailsMapper
import com.baatsen.venuesearch.data.service.FourSquareService
import com.baatsen.venuesearch.domain.model.Venue
import com.baatsen.venuesearch.domain.model.VenueDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single

class VenueDetailsRepository(
    private val fourSquareService: FourSquareService,
    private val venueDetailsMapper: VenueDetailsMapper,
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson

) {
    val VERSION_DATE = "20200701"

    fun getVenueDetails(venueId: String): Single<VenueDetails> {
        return fourSquareService.getVenueDetails(
            venueId = venueId,
            clientId = BuildConfig.CLIENT_ID,
            secretId = BuildConfig.SECRET_ID,
            versionDate = VERSION_DATE
        )
            .map { venueDetailsMapper.transform(it) }
            .doOnNext { store(venueId, it) }
            .onErrorReturn { getFromCache(venueId) }
            .singleOrError()
    }

    private fun getFromCache(venueId: String): VenueDetails {
        val json = sharedPreferences.getString(venueId, null)
        return gson.fromJson(json, VenueDetails::class.java)
    }

    private fun store(venueId: String, response: VenueDetails) {
        sharedPreferences.edit().putString(venueId, gson.toJson(response)).apply()
    }
}