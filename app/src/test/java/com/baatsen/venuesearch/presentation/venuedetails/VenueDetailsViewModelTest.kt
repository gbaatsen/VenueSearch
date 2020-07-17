package com.baatsen.venuesearch.presentation.venuedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.baatsen.venuesearch.ImmediateSchedulerProvider
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.domain.interactor.GetVenueDetailsService
import com.baatsen.venuesearch.domain.model.VenueDetails
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import retrofit2.HttpException
import retrofit2.Response

class VenueDetailsViewModelTest {
    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getVenueDetailsService: GetVenueDetailsService

    val photoUrls = listOf("http://www.1.nl", "http://www.2.nl")
    private lateinit var venueDetailsViewModel: VenueDetailsViewModel

    @Before
    fun setup() {
        venueDetailsViewModel =
            VenueDetailsViewModel(ImmediateSchedulerProvider, getVenueDetailsService)

    }

    @Test
    fun `venues are set when received`() {
        val venueDetails = VenueDetails(
            "13",
            "Best Something",
            "Blastraat 12, Amsterdam",
            "Best thing in town",
            "020 6 370 926", null, 4.5, photoUrls
        )

        whenever(getVenueDetailsService("12")).thenReturn(
            Single.just(venueDetails)

        )
        venueDetailsViewModel.getVenueDetails("12")
        assertEquals(venueDetailsViewModel.venueDetails.value, venueDetails)
    }

    @Test
    fun `right error is set when occurred http 403`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(403, responseBody)
        whenever(getVenueDetailsService("6572")).thenReturn(
            Single.error(IllegalStateException(HttpException(errorResponse)))

        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_limit_exceeded)
    }

    @Test
    fun `right error is set when occurred http 429`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(429, responseBody)
        whenever(getVenueDetailsService("6572")).thenReturn(
            Single.error(IllegalStateException(HttpException(errorResponse)))

        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_limit_exceeded)
    }

    @Test
    fun `right error is set when occurred http 500`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(500, responseBody)
        whenever(getVenueDetailsService("6572")).thenReturn(
            Single.error(IllegalStateException(HttpException(errorResponse)))

        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_loading_details)
    }

    @Test
    fun `right error is set when occurred any other error`() {
        whenever(getVenueDetailsService("6572")).thenReturn(
            Single.error(IllegalStateException())

        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_loading_details)
    }
}