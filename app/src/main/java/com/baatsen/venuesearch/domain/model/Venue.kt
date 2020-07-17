package com.baatsen.venuesearch.domain.model

data class Venue(
    val id: String,
    val name: String? = null,
    val location: String? = null
) {
    override fun toString(): String {
        return "$id,$name,$location"
    }
}