package com.baatsen.venuesearch.presentation.venuedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.baatsen.venuesearch.ImmediateSchedulerProvider
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.domain.interactor.GetVenueDetailsUseCase
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
import org.mockito.junit.MockitoRule
import retrofit2.HttpException
import retrofit2.Response

class VenueDetailsViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getVenueDetailsUseCase: GetVenueDetailsUseCase

    private val photoUrls = listOf("http://www.1.nl", "http://www.2.nl")
    private lateinit var venueDetailsViewModel: VenueDetailsViewModel

    @Before
    fun setup() {
        venueDetailsViewModel =
            VenueDetailsViewModel(ImmediateSchedulerProvider, getVenueDetailsUseCase)
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

        whenever(getVenueDetailsUseCase("12")).thenReturn(
            Single.just(venueDetails)
        )
        venueDetailsViewModel.getVenueDetails("12")
        assertEquals(venueDetailsViewModel.venueDetails.value, venueDetails)
    }

    @Test
    fun `right error is set when occurred http 403`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(403, responseBody)
        whenever(getVenueDetailsUseCase("6572")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_limit_exceeded)
    }

    @Test
    fun `right error is set when occurred http 429`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(429, responseBody)
        whenever(getVenueDetailsUseCase("6572")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_limit_exceeded)
    }

    @Test
    fun `right error is set when occurred http 500`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(500, responseBody)
        whenever(getVenueDetailsUseCase("6572")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_loading_details)
    }

    @Test
    fun `right error is set when occurred any other error`() {
        whenever(getVenueDetailsUseCase("6572")).thenReturn(
            Single.error(IllegalStateException())
        )
        venueDetailsViewModel.getVenueDetails("6572")
        assertEquals(venueDetailsViewModel.error.value, R.string.error_loading_details)
    }
}