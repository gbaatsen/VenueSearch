package com.baatsen.venuesearch.data.model

data class VenueLocation(
    val address: String? = null,
    val crossStreet: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val distance: Int? = null,
    val formattedAddress: List<String>? = null
) {
    override fun toString(): String {
        if (!formattedAddress.isNullOrEmpty()) return formattedAddress.joinToString()
        val result = ifNotNull(this.address) +
                ifNotNull(this.crossStreet) +
                ifNotNull(this.city) +
                ifNotNull(this.postalCode) +
                ifNotNull(this.country)
        return if (result.isNotEmpty()) result.substring(0, result.length - 2)
        else result
    }

    private fun ifNotNull(string: String?): String {
        string?.let { return "$it, " } ?: return ""
    }
}
