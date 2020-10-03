package com.example.takeahike.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.currentHikeUIEvents.LoadRouteEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.currentRoute.RecenterAction
import com.example.takeahike.viewData.editRoute.EditRouteData
import com.example.takeahike.viewData.editRoute.SaveCompleteAction
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class CurrentHikeViewModel(mapQuestKey: String, routeId: String)
    : ViewModel(), ActionPresenter<RecenterAction, CurrentHikeData> {

    override val action: MutableLiveData<ConsumableValue<RecenterAction>> by lazy {
        MutableLiveData<ConsumableValue<RecenterAction>>()
    }

    override val data: MutableLiveData<CurrentHikeData> by lazy {
        MutableLiveData<CurrentHikeData>()
    }

    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)
    private val dataParser : ParseRouteListData = ParseRouteListData()

    init {
        loadRoute(routeId)
    }

    override fun update(event: Any) {
        // TODO: Recenter action
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
            CurrentHikeData(
                points,
                road
            )
        }
        else {
            //TODO: Error handling
            CurrentHikeData(
                listOf(),
                Road()
            )
        }

        val recenterAction = route?.wayPoints?.firstOrNull()?.let {
            RecenterAction(it.lat, it.lon)
        }

        data.value = viewModel

        if (recenterAction != null) {
            action.value = ConsumableValue(recenterAction)
        }
    }
}