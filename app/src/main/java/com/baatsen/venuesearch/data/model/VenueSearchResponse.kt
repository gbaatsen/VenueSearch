package com.baatsen.venuesearch.data.model

data class VenueSearchResponse(
    val response: VenuesResponse
)

data class VenuesResponse(
    val venues: List<VenueJson>
)
