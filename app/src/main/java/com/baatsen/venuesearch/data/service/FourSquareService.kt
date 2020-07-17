package com.baatsen.venuesearch.data.service

import com.baatsen.venuesearch.data.model.VenueDetailsResponse
import com.baatsen.venuesearch.data.model.VenueSearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FourSquareService {
    /**
     * @param clientId - the client ID for FourSquare
     * @param secretId - the secret ID for FourSquare
     * @param near - the location to search e.g. "Amsterdam"
     * @param radius - the radius to search within in meters
     * @param limit - limit the number of results
     * @param versionDate - version date of the api, format YYYYMMDD
     */
    @GET("v2/venues/search")
    fun getVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") secretId: String,
        @Query("near") near: String,
        @Query("radius") radius: Int,
        @Query("limit") limit: Int,
        @Query("v") versionDate: String
    ): Observable<VenueSearchResponse>


    /**
     * @param venueId - the venue ID
     * @param clientId - the client ID for FourSquare
     * @param secretId - the secret ID for FourSquare
     * @param versionDate - version date of the api, format YYYYMMDD
     */
    @GET("v2/venues/{venueId}")
    fun getVenueDetails(
        @Path("venueId") venueId: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") secretId: String,
        @Query("v") versionDate: String
    ): Observable<VenueDetailsResponse>
}