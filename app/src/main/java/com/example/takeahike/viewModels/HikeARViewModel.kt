package com.example.takeahike.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.R
import com.example.takeahike.backend.data.LatLng
import com.example.takeahike.backend.utilities.LocationLogic
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.backend.utilities.loadCurrentHike
import com.example.takeahike.uiEvents.hikeAR.UIReadyEvent
import com.example.takeahike.viewData.hikeAR.HikeARData
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class HikeARViewModel(
        application: Application,
        mapQuestKey: String)
    : AndroidViewModel(application), Presenter<HikeARData> {

    private val dataParser : ParseRouteListData = ParseRouteListData()
    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)
    private val locationLogic = LocationLogic()

    override val data: MutableLiveData<HikeARData> by lazy {
        MutableLiveData<HikeARData>()
    }

    override fun update(event: Any) {
        if (event is UIReadyEvent) {
            setInitialLocation()
        }
    }

    private fun setInitialLocation() {
        val applicationContext = getApplication<Application>().applicationContext
        val currentHike = loadCurrentHike(
            applicationContext,
            applicationContext.resources.getString(R.string.current_hike_file_name))
        if (currentHike != null) {
            val route = dataParser.loadRoute(currentHike.routeId)
            if (route != null) {
                val points = ArrayList(route.wayPoints.map { GeoPoint(it.lat, it.lon) })
                val road = if (points.count() > 1) {
                    roadManager.getRoad(points)
                }
                else {
                    Road()
                }
                val currentPoint = locationLogic.getPointAlongRoad(road, currentHike.distanceTraveled)
                if (currentPoint != null) {
                    data.value = HikeARData(LatLng(
                        currentPoint.latitude,
                        currentPoint.longitude
                    ))
                }
            }
        }
    }
}