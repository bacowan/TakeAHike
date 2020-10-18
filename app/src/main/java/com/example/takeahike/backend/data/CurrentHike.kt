package com.example.takeahike.backend.data

import kotlinx.serialization.Serializable

@Serializable
class CurrentHike(
    var routeId: String,
    var lastRecordedLocation: LatLng? = null,
    var distanceTraveled: Double
)