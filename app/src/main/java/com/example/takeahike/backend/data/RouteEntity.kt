package com.example.takeahike.backend.data

import io.realm.RealmList
import io.realm.RealmObject

open class RouteEntity(
    var name: String = "",
    var road: RealmList<GpsPointEntity> = RealmList(),
    var wayPoints: RealmList<GpsPointEntity> = RealmList()
) : RealmObject()