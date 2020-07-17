package com.baatsen.venuesearch.presentation.venues

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.domain.model.Venue
import com.baatsen.venuesearch.presentation.venuedetails.VenueDetailActivity
import com.baatsen.venuesearch.setVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.venue_activity.*
import org.jetbrains.anko.intentFor
import org.koin.androidx.viewmodel.ext.android.viewModel

class VenueActivity : AppCompatActivity() {

    private val viewModel: VenueViewModel by viewModel()
    private val venueAdapter = VenueAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_activity)
        venues.adapter = venueAdapter
        venueAdapter.onVenueClicked = { onVenueClicked(it) }

        search_edit.setOnEditorActionListener { _, actionId, _ ->
            if ((actionId == EditorInfo.IME_ACTION_DONE || actionId == IME_ACTION_SEARCH) &&
                search_edit.text.toString().isNotEmpty()
            ) {
                viewModel.getVenues(search_edit.text.toString())
            }
            true
        }

        viewModel.venues.observe(this, Observer { onVenuesLoaded(it) })
        viewModel.isLoading.observe(this, Observer { showLoadingIndicator(it) })
        viewModel.error.observe(this, Observer { showErrorMessage(it) })
    }

    private fun onVenueClicked(venueId: String) {
        startActivity(intentFor<VenueDetailActivity>(VenueDetailActivity.VENUE_ID to venueId))
    }

    private fun showLoadingIndicator(isLoading: Boolean) {
        loading_indicator.setVisible(isLoading)
    }

    private fun onVenuesLoaded(venues: List<Venue>) {
        sad_droid.setVisible(false)
        venueAdapter.updateVenues(venues)
    }

    private fun showErrorMessage(errorResId: Int) {
        sad_droid.setVisible(true)
        Snackbar.make(sad_droid, errorResId, Snackbar.LENGTH_LONG)
            .show()
    }
}