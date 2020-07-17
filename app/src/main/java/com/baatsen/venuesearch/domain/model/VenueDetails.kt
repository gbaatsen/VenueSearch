package com.baatsen.venuesearch.domain.model

data class VenueDetails(
    val id: String,
    val name: String? = null,
    val location: String? = null,
    val description: String? = null,
    val phone: String? = null,
    val twitter: String? = null,
    val rating: Double?,
    val photoUrls: List<String>?
)