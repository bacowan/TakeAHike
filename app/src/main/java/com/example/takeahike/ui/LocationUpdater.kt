package com.example.takeahike.ui

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest

class LocationUpdater(
        private val fusedLocationClient: FusedLocationProviderClient,
        private val locationCallback: LocationCallback) {

    private var isRequestingLocationUpdates = false

    fun startLocationUpdates() {
        if (!isRequestingLocationUpdates) {
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.create()?.apply {
                    interval = 10000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                },
                locationCallback,
                Looper.getMainLooper() // TODO: how does this work?
            )
            isRequestingLocationUpdates = true
        }
    }

    fun endLocationUpdates() {
        isRequestingLocationUpdates = false
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}