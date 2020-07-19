package com.baatsen.venuesearch.data.repository

import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.data.model.VenueMapper
import com.baatsen.venuesearch.data.service.FourSquareApiConfig
import com.baatsen.venuesearch.domain.model.Venue
import io.reactivex.Single

private const val RADIUS = 10000
private const val LIMIT = 10
private const val VERSION_DATE = "20200701"

class VenueRepository(
    private val fourSquareApiConfig: FourSquareApiConfig,
    private val venueMapper: VenueMapper
) {
    fun getVenues(location: String): Single<List<Venue>> {
        return fourSquareApiConfig.get().getVenues(
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