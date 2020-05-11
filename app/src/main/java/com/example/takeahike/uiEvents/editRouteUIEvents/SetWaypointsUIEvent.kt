package com.example.takeahike.uiEvents.editRouteUIEvents

import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint

data class SetWaypointsUIEvent(
    val points : ArrayList<GeoPoint>,
    val newPoint : GeoPoint? = null)