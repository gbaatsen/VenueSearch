package com.baatsen.venuesearch.presentation.venuedetails

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.domain.model.VenueDetails
import com.baatsen.venuesearch.setVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.venue_detail_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class VenueDetailActivity : AppCompatActivity() {
    private val photoAdapter = PhotoAdapter()
    private val viewModel: VenueDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_detail_activity)

        val venueId = intent.getSerializableExtra(VENUE_ID) as String?
        photos.adapter = photoAdapter

        viewModel.venueDetails.observe(this, Observer { onVenueDetailsLoaded(it) })
        viewModel.isLoading.observe(this, Observer { showLoadingIndicator(it) })
        viewModel.error.observe(this, Observer { showErrorMessage(it) })

        venueId?.let { viewModel.getVenueDetails(it) }
    }

    private fun showErrorMessage(errorResId: Int) {
        sad_droid.setVisible(true)
        Snackbar.make(sad_droid, errorResId, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun onVenueDetailsLoaded(venueDetails: VenueDetails) {
        sad_droid.setVisible(false)
        with(venueDetails) {
            setTextOrHide(venue_name, name)
            setTextOrHide(venue_description, description)
            setTextOrHide(venue_address, location)
            setTextOrHide(venue_phone, phone)
            setTextOrHide(venue_twitter, twitter)
            setTextOrHide(venue_rating, rating?.toString())
            photoUrls?.let { photoAdapter.updatePhotos(it) }
        }
    }

    private fun setTextOrHide(textView: TextView, text: String?) =
        text?.let {
            textView.text = text
            textView.setVisible(true)
        } ?: textView.setVisible(false)

    private fun showLoadingIndicator(isLoading: Boolean) {
        loading_indicator.setVisible(isLoading)
    }

    companion object {
        const val VENUE_ID = "VENUE_ID"
    }
}