package com.example.takeahike.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.takeahike.R
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.backend.utilities.LocationLogic
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.currentHikeUIEvents.RecenterEvent
import com.example.takeahike.uiEvents.currentHikeUIEvents.UpdatePositionEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.currentRoute.RecenterAction
import com.example.takeahike.backend.utilities.*
import com.example.takeahike.uiEvents.currentHikeUIEvents.ViewLoadedEvent
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class CurrentHikeViewModel(application: Application)
    : AndroidViewModel(application), ActionPresenter<RecenterAction, CurrentHikeData> {

    private var currentHike : CurrentHike = CurrentHike("", null, 0.0)
    private var routePoints: List<GeoPoint> = listOf()
    private var road: List<GeoPoint> = listOf()
    private val dataParser : ParseRouteListData = ParseRouteListData()
    private val locationLogic = LocationLogic()

    override val action: MutableLiveData<ConsumableValue<RecenterAction>> by lazy {
        MutableLiveData<ConsumableValue<RecenterAction>>()
    }

    override val data: MutableLiveData<CurrentHikeData> by lazy {
        MutableLiveData<CurrentHikeData>()
    }

    override fun update(event: Any) {
        when (event) {
            is UpdatePositionEvent -> updatePosition(event)
            is RecenterEvent -> recenterView()
            is ViewLoadedEvent -> viewLoaded()
        }
    }

    private fun viewLoaded() {
        currentHike = loadCurrentHike()
        recenterView()
    }

    private fun loadCurrentHike() : CurrentHike {
        val applicationContext = getApplication<Application>().applicationContext
        val currentHike = loadCurrentHike(
            applicationContext,
            applicationContext.resources.getString(R.string.current_hike_file_name))
        val viewData = if (currentHike != null) {
            val route = dataParser.loadRoute(currentHike.routeId)
            if (route != null) {
                val points = ArrayList(route.wayPoints.map { GeoPoint(it.lat, it.lon) })
                road = route.road.map { GeoPoint(it.lat, it.lon) }
                val currentPoint = locationLogic.getPointAlongRoad(road, currentHike.distanceTraveled)

                CurrentHikeData(
                    points,
                    road,
                    currentPoint
                )
            }
            else {
                //TODO: Error handling
                CurrentHikeData(
                    listOf(),
                    listOf(),
                    null
                )
            }
        }
        else {
            //TODO: Error handling
            CurrentHikeData(
                listOf(),
                listOf(),
                null
            )
        }

        routePoints = viewData.waypoints

        data.value = viewData
        return CurrentHike(
            currentHike?.routeId ?: "",
            currentHike?.lastRecordedLocation,
            currentHike?.distanceTraveled ?: 0.0)
    }

    private fun updatePosition(positionEvent: UpdatePositionEvent) {
        if (locationLogic.updateCurrentHike(currentHike, positionEvent.location)) {
            data.value = CurrentHikeData(
                routePoints,
                road,
                locationLogic.getPointAlongRoad(road, currentHike.distanceTraveled))

            val applicationContext = getApplication<Application>().applicationContext
            saveCurrentHike(
                currentHike,
                applicationContext,
                applicationContext.resources.getString(R.string.current_hike_file_name))
        }
    }

    private fun recenterView() {
        locationLogic.getPointAlongRoad(road, currentHike.distanceTraveled)?.let {
            action.value = ConsumableValue(RecenterAction(it.latitude, it.longitude))
        }
    }
}