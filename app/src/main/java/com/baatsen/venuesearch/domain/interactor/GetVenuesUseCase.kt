package com.baatsen.venuesearch.domain.interactor

import com.baatsen.venuesearch.data.repository.VenueRepository
import com.baatsen.venuesearch.domain.model.Venue
import io.reactivex.Single

class GetVenuesUseCase(private val repository: VenueRepository) {
    operator fun invoke(location: String): Single<List<Venue>> {
        return repository.getVenues(location)
    }
}