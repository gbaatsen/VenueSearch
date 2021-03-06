package com.baatsen.venuesearch.presentation.venuedetails

import androidx.lifecycle.ViewModel
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.SchedulerProvider
import com.baatsen.venuesearch.SingleLiveEvent
import com.baatsen.venuesearch.domain.interactor.GetVenueDetailsUseCase
import com.baatsen.venuesearch.domain.model.VenueDetails
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

class VenueDetailsViewModel(
    private val scheduler: SchedulerProvider,
    private val getVenueDetailsUseCase: GetVenueDetailsUseCase
) : ViewModel() {

    private lateinit var subscription: Disposable
    val venueDetails = SingleLiveEvent<VenueDetails>()
    val error = SingleLiveEvent<Int>()
    val isLoading = SingleLiveEvent<Boolean>()

    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getVenueDetails(venueId: String) {
        subscription = getVenueDetailsUseCase(venueId)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { isLoading.postValue(true) }
            .doAfterTerminate { isLoading.postValue(false) }
            .subscribe(
                { venueDetails -> onVenuesReceived(venueDetails) },
                { t -> onError(t) }
            )
    }

    private fun onVenuesReceived(venueDetails: VenueDetails) {
        this.venueDetails.postValue(venueDetails)
    }

    private fun onError(t: Throwable) {
        if (t is HttpException) {
            when (t .code()) {
                403, 429 -> error.postValue(R.string.error_limit_exceeded)
                else -> error.postValue(R.string.error_loading_details)
            }
            return
        }
        error.postValue(R.string.error_loading_details)
    }
}