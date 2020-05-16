package com.example.takeahike.backend.data

import io.realm.RealmObject

open class GpsPointEntity(
    var lat: Double = 0.0,
    var lon: Double = 0.0
) : RealmObject()