package com.example.takeahike.viewmodels.editRoute

import org.osmdroid.api.IGeoPoint
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

class EditRouteViewModel(
    val waypoints : List<GeoPoint>,
    val road : Road
)