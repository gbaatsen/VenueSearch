package com.baatsen.venuesearch.presentation.venues

import androidx.lifecycle.ViewModel
import com.baatsen.venuesearch.R
import com.baatsen.venuesearch.SchedulerProvider
import com.baatsen.venuesearch.SingleLiveEvent
import com.baatsen.venuesearch.domain.interactor.GetVenuesService
import com.baatsen.venuesearch.domain.model.Venue
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

class VenueViewModel(
    private val scheduler: SchedulerProvider,
    private val getVenuesService: GetVenuesService
) : ViewModel() {

    private lateinit var subscription: Disposable
    val venues = SingleLiveEvent<List<Venue>>()
    val isLoading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Int>()


    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getVenues(location: String) {
        this.venues.value = emptyList()
        subscription = getVenuesService(location)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doOnSubscribe { isLoading.postValue(true) }
            .doAfterTerminate { isLoading.postValue(false) }
            .subscribe(
                { venues -> onVenuesReceived(venues) },
                { t -> onError(t) }
            )
    }

    private fun onError(t: Throwable) {
        if (t.cause is HttpException) {
            when ((t.cause as HttpException).code()) {
                400 -> error.postValue(R.string.error_location_not_found)
                403, 429 -> error.postValue(R.string.error_limit_exceeded)
                else -> error.postValue(R.string.error_loading_venues)
            }
            return
        }
        error.postValue(R.string.error_loading_venues)
    }

    private fun onVenuesReceived(venues: List<Venue>) {
        this.venues.postValue(venues)
    }
}