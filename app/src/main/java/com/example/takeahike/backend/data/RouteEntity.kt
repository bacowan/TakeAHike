package com.example.takeahike.backend.data

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class RouteEntity(
    @PrimaryKey @Required
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var road: RealmList<GpsPointEntity> = RealmList(),
    var wayPoints: RealmList<GpsPointEntity> = RealmList()
) : RealmObject()