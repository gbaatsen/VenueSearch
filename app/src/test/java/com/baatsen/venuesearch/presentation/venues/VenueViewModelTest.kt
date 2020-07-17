package com.baatsen.venuesearch.presentation.venuedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.baatsen.venuesearch.ImmediateSchedulerProvider
import com.baatsen.venuesearch.domain.interactor.GetVenuesService
import com.baatsen.venuesearch.domain.model.Venue
import com.baatsen.venuesearch.presentation.venues.VenueViewModel
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit

class VenueViewModelTest {
    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @get:Rule
    var testRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getVenueService: GetVenuesService

    private lateinit var venueViewModel: VenueViewModel

    @Before
    fun setup() {
        venueViewModel =
            VenueViewModel(ImmediateSchedulerProvider, getVenueService)

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

        whenever(getVenueService("Amsterdam")).thenReturn(
            Single.just(listOf(venue1, venue2))

        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.venues.value, listOf(venue1, venue2))
    }

    @Test
    fun `error is set when occurred`() {
        whenever(getVenueService("Amsterdam")).thenReturn(
            Single.error(IllegalStateException())

        )
        venueViewModel.getVenues("Amsterdam")
        assertEquals(venueViewModel.error.value, true)
    }
}