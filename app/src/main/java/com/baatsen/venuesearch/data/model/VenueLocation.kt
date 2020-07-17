package com.baatsen.venuesearch.data.model

data class VenueLocation(
    val address: String?,
    val crossStreet: String?,
    val city: String?,
    val state: String?,
    val postalCode: String?,
    val country: String?,
    val lat: Double?,
    val lng: Double?,
    val distance: Int?,
    val formattedAddress: List<String>?
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
