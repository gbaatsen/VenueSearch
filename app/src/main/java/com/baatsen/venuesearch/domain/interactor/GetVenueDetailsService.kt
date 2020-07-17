package com.baatsen.venuesearch.domain.interactor

import com.baatsen.venuesearch.data.repository.VenueDetailsRepository
import com.baatsen.venuesearch.domain.model.VenueDetails
import io.reactivex.Single

class GetVenueDetailsService(private val repository: VenueDetailsRepository) {

    operator fun invoke(venueId: String): Single<VenueDetails> {
        return repository.getVenueDetails(venueId)
    }
}