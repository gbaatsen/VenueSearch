package com.baatsen.venuesearch.data.repository

import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.data.model.VenueDetailsMapper
import com.baatsen.venuesearch.data.service.FourSquareApi
import com.baatsen.venuesearch.domain.model.VenueDetails
import io.reactivex.Single

private const val VERSION_DATE = "20200701"

class VenueDetailsRepository(
    private val fourSquareApi: FourSquareApi,
    private val venueDetailsMapper: VenueDetailsMapper
) {
    fun getVenueDetails(venueId: String): Single<VenueDetails> {
        return fourSquareApi.getVenueDetails(
            venueId = venueId,
            clientId = BuildConfig.CLIENT_ID,
            secretId = BuildConfig.SECRET_ID,
            versionDate = VERSION_DATE
        )
            .map { venueDetailsMapper.transform(it) }
            .singleOrError()
    }
}