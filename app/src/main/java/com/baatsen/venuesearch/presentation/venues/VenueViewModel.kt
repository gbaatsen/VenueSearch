package com.baatsen.venuesearch.presentation.venues

import androidx.lifecycle.ViewModel
import com.baatsen.venuesearch.SchedulerProvider
import com.baatsen.venuesearch.SingleLiveEvent
import com.baatsen.venuesearch.domain.model.Venue
import com.baatsen.venuesearch.domain.interactor.GetVenuesService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class VenueViewModel(
    private val scheduler: SchedulerProvider,
    private val getVenuesService: GetVenuesService
) : ViewModel() {

    private lateinit var subscription: Disposable
    val venues = SingleLiveEvent<List<Venue>>()
    val isLoading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Boolean>()


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
                { onError() }
            )
    }

    private fun onError() {
        error.postValue(true)
    }

    private fun onVenuesReceived(venues: List<Venue>) {
        this.venues.postValue(venues)
    }
}