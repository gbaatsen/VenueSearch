package com.baatsen.venuesearch.presentation.venues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.baatsen.venuesearch.ImmediateSchedulerProvider
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.domain.interactor.GetVenuesUseCase
import com.baatsen.venuesearch.domain.model.Venue
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

class VenueViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getVenueUseCase: GetVenuesUseCase

    private lateinit var venueViewModel: VenueViewModel

    @Before
    fun setup() {
        venueViewModel =
            VenueViewModel(ImmediateSchedulerProvider, getVenueUseCase)

    }

    @Test
    fun `venues are set when received`() {
        val venue1 = Venue(
            id = "13",
            name = "Best Something",
            location = "Blastraat 12, Amsterdam"
        )
        val venue2 = Venue(
            id = "14",
            name = "Second Best Something",
            location = "Blastraat 14, Amsterdam"
        )

        whenever(getVenueUseCase("Amsterdam")).thenReturn(
            Single.just(listOf(venue1, venue2))

        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.venues.value, listOf(venue1, venue2))
    }

    @Test
    fun `right error is set when occurred http 400`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(400, responseBody)
        whenever(getVenueUseCase("Amsterdram")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueViewModel.getVenues("Amsterdram")
        assertEquals(venueViewModel.error.value, R.string.error_location_not_found)
    }

    @Test
    fun `right error is set when occurred http 403`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(403, responseBody)
        whenever(getVenueUseCase("Amsterdam")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.error.value, R.string.error_limit_exceeded)
    }

    @Test
    fun `right error is set when occurred http 429`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(429, responseBody)
        whenever(getVenueUseCase("Amsterdam")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.error.value, R.string.error_limit_exceeded)
    }

    @Test
    fun `right error is set when occurred http 500`() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "")
        val errorResponse = Response.error<Any>(500, responseBody)
        whenever(getVenueUseCase("Amsterdam")).thenReturn(
            Single.error(HttpException(errorResponse))
        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.error.value, R.string.error_loading_venues)
    }

    @Test
    fun `right error is set when occurred any other error`() {
        whenever(getVenueUseCase("Amsterdam")).thenReturn(
            Single.error(IllegalStateException())

        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.error.value, R.string.error_loading_venues)
    }
}