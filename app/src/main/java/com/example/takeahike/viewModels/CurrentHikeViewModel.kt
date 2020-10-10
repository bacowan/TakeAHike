package com.example.takeahike.viewModels

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.backend.utilities.LocationLogic
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.currentHikeUIEvents.RecenterEvent
import com.example.takeahike.uiEvents.currentHikeUIEvents.UpdatePositionEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.currentRoute.RecenterAction
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class CurrentHikeViewModel(mapQuestKey: String, routeId: String)
    : ViewModel(), ActionPresenter<RecenterAction, CurrentHikeData> {

    private var distanceTraveled : Double = 0.0
    private var lastRecordedLocation: Location? = null
    private var routePoints: List<GeoPoint> = listOf()
    private var road: Road = Road()
    private var currentHikePosition: GeoPoint? = null

    override val action: MutableLiveData<ConsumableValue<RecenterAction>> by lazy {
        MutableLiveData<ConsumableValue<RecenterAction>>()
    }

    override val data: MutableLiveData<CurrentHikeData> by lazy {
        MutableLiveData<CurrentHikeData>()
    }

    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)
    private val dataParser : ParseRouteListData = ParseRouteListData()
    private val locationLogic = LocationLogic()

    init {
        loadRoute(routeId)
    }

    override fun update(event: Any) {
        if (event is UpdatePositionEvent) {
            updatePosition(event)
        }
        else if (event is RecenterEvent) {
            recenterView()
        }
    }

    private fun loadRoute(routeId: String) {
        val route = dataParser.loadRoute(routeId)
        val viewModel = if (route != null) {
            val points = ArrayList(route.wayPoints.map { GeoPoint(it.lat, it.lon) })
            val road = if (points.count() > 1) {
                roadManager.getRoad(points)
            }
            else {
                Road()
            }
            val currentPoint = locationLogic.getPointAlongRoad(road, distanceTraveled)

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
                Road(),
                null
            )
        }

        road = viewModel.road
        routePoints = viewModel.waypoints
        currentHikePosition = viewModel.currentPosition

        data.value = viewModel
        recenterView()
    }

    private fun updatePosition(positionEvent: UpdatePositionEvent) {
        val lastLocation = lastRecordedLocation
        if (lastLocation == null) {
            lastRecordedLocation = positionEvent.location
        }
        else {
            val distanceFromLastLocation = locationLogic.distanceBetween(lastLocation, positionEvent.location)
            if (distanceFromLastLocation != null && distanceFromLastLocation > 15) {
                distanceTraveled += distanceFromLastLocation
                lastRecordedLocation = positionEvent.location
                data.value = CurrentHikeData(
                    routePoints,
                    road,
                    locationLogic.getPointAlongRoad(road, distanceTraveled)
                )
            }
        }
    }

    private fun recenterView() {
        currentHikePosition?.let {
            action.value = ConsumableValue(RecenterAction(it.latitude, it.longitude))
        }
    }
}