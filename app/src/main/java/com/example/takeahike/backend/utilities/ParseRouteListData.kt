package com.example.takeahike.backend.utilities

import android.os.Parcel
import com.example.takeahike.backend.data.GpsPointEntity
import com.example.takeahike.backend.data.RouteEntity
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

class ParseRouteListData {
    fun loadData() : List<RouteEntity> {
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
        entity.road.addAll(road.mNodes.map { node ->
            GpsPointEntity(node.mLocation.latitude, node.mLocation.longitude)
        })
        entity.wayPoints.addAll(wayPoints.map { node ->
            GpsPointEntity(node.latitude, node.longitude)
        })

        realm.beginTransaction()
        realm.copyToRealm(entity)
        realm.commitTransaction()
    }
}