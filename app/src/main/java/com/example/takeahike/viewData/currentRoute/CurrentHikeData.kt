package com.example.takeahike.viewData.currentRoute

import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

data class CurrentHikeData(
    val waypoints : List<GeoPoint>,
    val road : List<GeoPoint>,
    val currentPosition: GeoPoint?
)