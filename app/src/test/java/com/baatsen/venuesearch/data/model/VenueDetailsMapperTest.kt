package com.baatsen.venuesearch.data.model

import com.baatsen.venuesearch.domain.model.VenueDetails
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class VenueDetailsMapperTest {
    private val venueDetailsMapper = VenueDetailsMapper()

    @Test
    fun `formatted phone number is returned when available`() {
        val venueDetailsResponse = VenueDetailsResponse(
            Response(
                VenueDetailsJson(
                    id = "13",
                    name = "Best Something",
                    location = VenueLocation(address = "Blastraat 12, Amsterdam"),
                    contact = Contact(formattedPhone = "020 6 370 926", phone = "0206370926"),
                    description = "Best thing in town",
                    rating = 4.5
                )
            )
        )


        val result = venueDetailsMapper.transform(
            venueDetailsResponse
        )

        assertEquals(
            result, VenueDetails(
                "13",
                "Best Something",
                "Blastraat 12, Amsterdam",
                "Best thing in town",
                "020 6 370 926", null, 4.5, null
            )
        )
    }

    @Test
    fun `unformatted phone number is returned when formatted one is not available`() {
        val venueDetailsResponse = VenueDetailsResponse(
            Response(
                VenueDetailsJson(
                    id = "13",
                    name = "Best Something",
                    location = VenueLocation(address = "Blastraat 12, Amsterdam"),
                    contact = Contact(phone = "0206370926"),
                    description = "Best thing in town",
                    rating = 4.5
                )
            )
        )


        val result = venueDetailsMapper.transform(
            venueDetailsResponse
        )

        assertEquals(
            result, VenueDetails(
                "13",
                "Best Something",
                "Blastraat 12, Amsterdam",
                "Best thing in town",
                "0206370926", null, 4.5, null
            )
        )
    }

}
