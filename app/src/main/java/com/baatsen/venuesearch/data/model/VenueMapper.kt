package com.baatsen.venuesearch.data.model

import com.baatsen.venuesearch.domain.model.Venue

class VenueMapper {
    fun transform(venueSearchResponse: VenueSearchResponse): List<Venue> {
        return venueSearchResponse.response.venues.map {
            Venue(
                id = it.id,
                name = it.name,
                location = it.location.toString()
            )
        }
    }
}