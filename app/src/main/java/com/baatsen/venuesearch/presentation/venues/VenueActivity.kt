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
import kotlinx.android.synthetic.main.venue_detail_activity.*
import kotlinx.android.synthetic.main.venue_search_activity.*
import kotlinx.android.synthetic.main.venue_search_activity.loading_indicator
import org.jetbrains.anko.intentFor
import org.koin.androidx.viewmodel.ext.android.viewModel

class VenueActivity : AppCompatActivity() {

    private val viewModel: VenueViewModel by viewModel()
    private val venueAdapter = VenueAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_search_activity)
        venues.adapter = venueAdapter
        venueAdapter.onVenueClicked = { onVenueClicked(it) }

        search_edit.setText("Amsterdam")

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
        viewModel.error.observe(this, Observer { showErrorMessage() })
    }

    private fun onVenueClicked(venueId: String) {
        startActivity(intentFor<VenueDetailActivity>(VenueDetailActivity.VENUE_ID to venueId))
    }

    private fun showLoadingIndicator(isLoading: Boolean) {
        loading_indicator.setVisible(isLoading)
    }

    private fun onVenuesLoaded(venues: List<Venue>) {
        venueAdapter.updateVenues(venues)
    }

    private fun showErrorMessage() {
        sad_droid.setVisible(true)
        Snackbar.make(sad_droid, R.string.error_loading_details, Snackbar.LENGTH_LONG)
            .show()
    }
}