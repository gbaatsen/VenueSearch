package com.baatsen.venuesearch.data.repository

import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.data.model.VenueMapper
import com.baatsen.venuesearch.data.service.FourSquareApiConfig
import com.baatsen.venuesearch.domain.model.Venue
import io.reactivex.Single


class VenueRepository(
    private val fourSquareApiConfig: FourSquareApiConfig,
    private val venueMapper: VenueMapper
) {
    val RADIUS = 10000
    val LIMIT = 10
    val VERSION_DATE = "20200701"

    fun getVenues(location: String): Single<List<Venue>> {
        return fourSquareApiConfig.create().getVenues(
            clientId = BuildConfig.CLIENT_ID,
            secretId = BuildConfig.SECRET_ID,
            near = location,
            radius = RADIUS,
            limit = LIMIT,
            versionDate = VERSION_DATE
        )
            .map { venueMapper.transform(it) }
            .singleOrError()
    }
}