package com.example.takeahike.viewData.editRoute

import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

class EditRouteData(
    val waypoints : List<GeoPoint>,
    val road : Road
)