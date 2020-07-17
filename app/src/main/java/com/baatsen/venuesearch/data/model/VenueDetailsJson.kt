package com.baatsen.venuesearch.data.model

data class VenueDetailsJson(
    val id: String,
    val name: String,
    val location: VenueLocation,
    val description: String?,
    val contact: Contact,
    val rating: Double?,
    val photos: Photos?
)

data class Photos(
    val groups: List<Group>?
) {
    fun toUrlList(): List<String> {
        val result = mutableListOf<String>()
        groups?.forEach {
            it.items.forEach { result.add(it.prefix + "height500" + it.suffix) }
        }
        return result
    }
}

data class Group(
    val items: List<Photo>
)


data class Photo(
    val prefix: String?,
    val suffix: String?
)

data class Contact(
    val phone: String?,
    val formattedPhone: String?,
    val twitter: String?
)