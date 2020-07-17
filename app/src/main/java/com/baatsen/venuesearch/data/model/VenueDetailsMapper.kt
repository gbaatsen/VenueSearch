package com.baatsen.venuesearch.data.model

import com.baatsen.venuesearch.domain.model.VenueDetails

class VenueDetailsMapper() {
    fun transform(venueDetailsResponse: VenueDetailsResponse): VenueDetails {
        val response = venueDetailsResponse.response.venue
        return VenueDetails(
            id = response.id,
            name = response.name,
            location = response.location.toString(),
            description = response.description,
            phone = response.contact.formattedPhone ?: response.contact.phone,
            twitter = response.contact.twitter,
            rating = response.rating,
            photoUrls = response.photos?.toUrlList()
        )
    }
}