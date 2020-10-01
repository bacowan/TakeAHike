package com.example.takeahike.viewmodels.currentRoute

import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

data class CurrentHikeViewModel(
    val waypoints : List<GeoPoint>,
    val road : Road
)