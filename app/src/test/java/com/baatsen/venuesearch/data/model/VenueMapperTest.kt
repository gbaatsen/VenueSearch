package com.baatsen.venuesearch.data.model

import com.baatsen.venuesearch.domain.model.Venue
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenueMapperTest {
    private val venueMapper = VenueMapper()

    @Test
    fun `formatted address is returned when available`() {
        val venueJson =
            VenueJson(
                "13", "CoolCat", VenueLocation(
                    address = "Kadijk 12",
                    city = "Amsterdam",
                    postalCode = "1026BX",
                    country = "Nederland",
                    formattedAddress = listOf("Kadijk 12", "1026BX", "Amsterdam")
                )
            )


        val venueSearchResponse = VenueSearchResponse(
            VenuesResponse(listOf(venueJson))
        )


        val result = venueMapper.transform(
            venueSearchResponse
        ).first()

        assertEquals(
            result, Venue(
                id = "13",
                name = "CoolCat",
                location = "Kadijk 12, 1026BX, Amsterdam"
            )
        )
    }

    @Test
    fun `address is returned formatted when formatted address is not available`() {
        val venueJson =
            VenueJson(
                "13", "CoolCat", VenueLocation(
                    address = "Kadijk 12",
                    city = "Amsterdam",
                    postalCode = "1026BX",
                    country = "Nederland"
                )
            )


        val venueSearchResponse = VenueSearchResponse(
            VenuesResponse(listOf(venueJson))
        )


        val result = venueMapper.transform(
            venueSearchResponse
        ).first()

        assertEquals(
            result, Venue(
                id = "13",
                name = "CoolCat",
                location = "Kadijk 12, Amsterdam, 1026BX, Nederland"
            )
        )
    }
}
