package com.baatsen.venuesearch.data.model

data class VenueDetailsResponse(
    val response: Response
)

data class Response(
    val venue: VenueDetailsJson
)
