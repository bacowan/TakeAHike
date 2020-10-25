package com.example.takeahike.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.R
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.backend.data.LatLng
import com.example.takeahike.backend.utilities.LocationLogic
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.backend.utilities.loadCurrentHike
import com.example.takeahike.backend.utilities.saveCurrentHike
import com.example.takeahike.uiEvents.hikeAR.UIReadyEvent
import com.example.takeahike.uiEvents.hikeAR.UpdatePositionEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.hikeAR.HikeARData
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class HikeARViewModel(application: Application)
    : AndroidViewModel(application), Presenter<HikeARData> {

    private var currentHike : CurrentHike = CurrentHike("", null, 0.0)
    private val dataParser : ParseRouteListData = ParseRouteListData()
    private val locationLogic = LocationLogic()
    private var road: List<GeoPoint> = listOf()

    override val data: MutableLiveData<HikeARData> by lazy {
        MutableLiveData<HikeARData>()
    }

    override fun update(event: Any) {
        when (event) {
            is UIReadyEvent -> setInitialLocation()
            is UpdatePositionEvent -> updatePosition(event)
        }
    }

    private fun updatePosition(event: UpdatePositionEvent) {
        if (locationLogic.updateCurrentHike(currentHike, event.location)) {
            val point = locationLogic.getPointAlongRoad(road, currentHike.distanceTraveled)
            if (point != null) {
                data.value = HikeARData(LatLng(
                    point.latitude,
                    point.longitude
                ))
            }

            val applicationContext = getApplication<Application>().applicationContext
            saveCurrentHike(
                currentHike,
                applicationContext,
                applicationContext.resources.getString(R.string.current_hike_file_name))
        }
    }

    private fun setInitialLocation() {
        val applicationContext = getApplication<Application>().applicationContext
        val hike = loadCurrentHike(
            applicationContext,
            applicationContext.resources.getString(R.string.current_hike_file_name))
        if (hike != null) {
            val route = dataParser.loadRoute(hike.routeId)
            if (route != null) {
                val points = ArrayList(route.wayPoints.map { GeoPoint(it.lat, it.lon) })
                road = ArrayList(route.road.map { GeoPoint(it.lat, it.lon) })
                val currentPoint = locationLogic.getPointAlongRoad(road, hike.distanceTraveled)
                if (currentPoint != null) {
                    data.value = HikeARData(LatLng(
                        currentPoint.latitude,
                        currentPoint.longitude
                    ))
                }
            }
            currentHike = hike
        }
    }
}