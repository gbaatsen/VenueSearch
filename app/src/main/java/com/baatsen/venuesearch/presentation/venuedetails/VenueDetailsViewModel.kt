package com.baatsen.venuesearch.presentation.venuedetails

import androidx.lifecycle.ViewModel
import com.baatsen.venuesearch.SchedulerProvider
import com.baatsen.venuesearch.SingleLiveEvent
import com.baatsen.venuesearch.domain.interactor.GetVenueDetailsService
import com.baatsen.venuesearch.domain.model.Photo
import com.baatsen.venuesearch.domain.model.VenueDetails
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class VenueDetailsViewModel(
    private val scheduler: SchedulerProvider,
    private val getVenueDetailsService: GetVenueDetailsService
) : ViewModel() {

    private lateinit var subscription: Disposable
    val venueDetails = SingleLiveEvent<VenueDetails>()
    val error = SingleLiveEvent<Boolean>()
    val isLoading = SingleLiveEvent<Boolean>()

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getVenueDetails(venueId: String) {
        subscription = getVenueDetailsService(venueId)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { isLoading.postValue(true) }
            .doAfterTerminate { isLoading.postValue(false) }
            .subscribe(
                { venueDetails -> onVenuesReceived(venueDetails) },
                { onError() }
            )
    }

    private fun onVenuesReceived(venueDetails: VenueDetails) {
        this.venueDetails.postValue(venueDetails)
    }

    private fun onError() {
        error.postValue(true)
    }
}