package com.baatsen.venuesearch

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

private const val SEVEN_DAYS = 60 * 60 * 24 * 7
private const val FIVE_SECONDS = 5

class CacheInterceptor(private val connectivityManager: ConnectivityManager) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (isConnected()) {
            request.newBuilder().header("Cache-Control", "public, max-age=$FIVE_SECONDS").build()
        } else {
            request.newBuilder()
            request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$SEVEN_DAYS"
                )
                .build()
        }
        return chain.proceed(request).newBuilder().build()
    }


    private fun isConnected(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else return false
    }
}