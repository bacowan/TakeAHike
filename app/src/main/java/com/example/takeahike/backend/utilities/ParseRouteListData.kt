package com.example.takeahike.backend.utilities

import android.os.Parcel
import android.util.Log
import com.example.takeahike.backend.data.GpsPointEntity
import com.example.takeahike.backend.data.RouteEntity
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

class ParseRouteListData {
    fun loadRoute(id: String) : RouteEntity? {
        val realm = Realm.getDefaultInstance()
        return realm.where<RouteEntity>().equalTo(RouteEntity::id.name, id).findFirst()
    }

    fun loadData() : List<RouteEntity> {
        // TODO: error handling for when the database can't be opened
        val realm = Realm.getDefaultInstance()
        return realm.where<RouteEntity>().findAll()
    }

    fun saveData(
        name: String,
        road: Road,
        wayPoints: List<GeoPoint>
    ) {
        val realm = Realm.getDefaultInstance()
        val entity = RouteEntity(name = name)

        entity.road.addAll(road.mRouteHigh.map { node ->
            GpsPointEntity(node.latitude, node.longitude)
        })
        entity.wayPoints.addAll(wayPoints.map { node ->
            GpsPointEntity(node.latitude, node.longitude)
        })

        realm.beginTransaction()
        realm.insert(entity)
        realm.commitTransaction()
    }
}