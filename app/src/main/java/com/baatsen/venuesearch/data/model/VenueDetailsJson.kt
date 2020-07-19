package com.baatsen.venuesearch.data.model

data class VenueDetailsJson(
    val id: String,
    val name: String,
    val location: VenueLocation? = null,
    val description: String? = null,
    val contact: Contact? = null,
    val rating: Double? = null,
    val photos: Photos? = null
)

data class Photos(
    val groups: List<Group>?
) {
    fun toUrlList(): List<String> {
        val result = mutableListOf<String>()
        groups?.forEach { group ->
            group.items.forEach { photo -> result.add(photo.prefix + "height300" + photo.suffix) }
        }
        return result
    }
}

data class Group(
    val items: List<Photo>
)


data class Photo(
    val prefix: String? = null,
    val suffix: String? = null
)

data class Contact(
    val phone: String? = null,
    val formattedPhone: String? = null,
    val twitter: String? = null
)