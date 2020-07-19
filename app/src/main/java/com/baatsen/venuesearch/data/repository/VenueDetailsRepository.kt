package com.baatsen.venuesearch.data.repository

import com.baatsen.venuesearch.BuildConfig
import com.baatsen.venuesearch.data.model.VenueDetailsMapper
import com.baatsen.venuesearch.data.service.FourSquareApiConfig
import com.baatsen.venuesearch.domain.model.VenueDetails
import io.reactivex.Single

private const val VERSION_DATE = "20200701"

class VenueDetailsRepository(
    private val fourSquareApiConfig: FourSquareApiConfig,
    private val venueDetailsMapper: VenueDetailsMapper
) {

    fun getVenueDetails(venueId: String): Single<VenueDetails> {
        return fourSquareApiConfig.get().getVenueDetails(
            venueId = venueId,
            clientId = BuildConfig.CLIENT_ID,
            secretId = BuildConfig.SECRET_ID,
            versionDate = VERSION_DATE
        )
            .map { venueDetailsMapper.transform(it) }
            .singleOrError()
    }
}