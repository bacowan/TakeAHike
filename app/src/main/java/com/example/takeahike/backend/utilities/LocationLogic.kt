package com.example.takeahike.backend.utilities

import android.location.Location
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.backend.data.LatLng
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint
import kotlin.math.*

class LocationLogic {
    fun getPointAlongRoad(road: Road, distance: Double): GeoPoint? {
        var distanceLeft = distance


        for (i in 1 until road.mRouteHigh.size) {
            val previousNode = road.mRouteHigh.elementAtOrNull(i - 1)
            val currentNode = road.mRouteHigh.elementAtOrNull(i)
            val currentSectionDistance = if (previousNode != null && currentNode != null) {
                distanceBetween(previousNode, currentNode)
            }
            else {
                null
            }

            if (currentSectionDistance != null && currentNode != null && previousNode != null) {
                if (distanceLeft <= currentSectionDistance) {
                    return if (distanceLeft > 0) {
                        getPointBetween(previousNode, currentNode, distanceLeft / currentSectionDistance)
                    } else {
                        previousNode
                    }
                }
                else {
                    distanceLeft -= currentSectionDistance
                }
            }
            else {
                break
            }
        }

        return road.mNodes.lastOrNull()?.mLocation
    }

    fun distanceBetween(p1: GeoPoint, p2: GeoPoint) : Double? {
        return distanceBetween(p1.latitude, p1.longitude, p2.latitude, p2.longitude)
    }

    fun distanceBetween(p1: LatLng, p2: LatLng) : Double? {
        return distanceBetween(p1.lat, p1.lon, p2.lat, p2.lon)
    }

    fun distanceBetween(p1: GeoPoint, p2: LatLng) : Double? {
        return distanceBetween(p1.latitude, p1.longitude, p2.lat, p2.lon)
    }

    fun distanceBetween(p1: LatLng, p2: GeoPoint) : Double? {
        return distanceBetween(p1.lat, p1.lon, p2.latitude, p2.longitude)
    }

    private fun distanceBetween(p1Lat: Double, p1Lon: Double, p2Lat: Double, p2Lon: Double) : Double? {
        val dist = FloatArray(1)
        Location.distanceBetween(p1Lat, p1Lon, p2Lat, p2Lon, dist)
        return dist.elementAtOrNull(0)?.toDouble()
    }

    private fun getPointBetween(p1: GeoPoint, p2: GeoPoint, weight: Double): GeoPoint {
        val p1Lat = p1.latitude * Math.PI / 180
        val p2Lat = p2.latitude * Math.PI / 180
        val p1Lon = p1.longitude * Math.PI / 180
        val p2Lon = p2.longitude * Math.PI / 180

        val x = ((cos(p1Lat) * cos(p1Lon) * (1 - weight)) + (cos(p2Lat) * cos(p2Lon) * weight))
        val y = ((cos(p1Lat) * sin(p1Lon) * (1 - weight)) + (cos(p2Lat) * sin(p2Lon) * weight))
        val z = (sin(p1Lat) * (1 - weight) + sin(p2Lat) * weight)

        val centerLon = atan2(y, x)
        val centerSqrt = sqrt(x * x + y * y)
        val centerLat = atan2(z, centerSqrt)

        return GeoPoint(centerLat * 180 / Math.PI, centerLon * 180 / Math.PI)
    }

    fun updateCurrentHike(
        currentHike: CurrentHike,
        location: LatLng,
    ) : Boolean {
        val lastRecordedLocation = currentHike.lastRecordedLocation
        return if (lastRecordedLocation == null) {
            currentHike.lastRecordedLocation = location
            true
        } else {
            val distanceFromLastLocation = distanceBetween(lastRecordedLocation, location)
            if (distanceFromLastLocation != null && distanceFromLastLocation > 15) {
                currentHike.distanceTraveled += distanceFromLastLocation
                currentHike.lastRecordedLocation = location
                true
            } else {
                false
            }
        }
    }
}