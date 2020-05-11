package com.example.takeahike.uiEvents.editRouteUIEvents

data class AddWaypointEvent(
    val lat : Double,
    val lon : Double,
    val index : Int = -1)