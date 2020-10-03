package com.example.takeahike.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.editRouteUIEvents.LoadRouteEvent
import com.example.takeahike.uiEvents.editRouteUIEvents.SaveEvent
import com.example.takeahike.uiEvents.editRouteUIEvents.SetWaypointsUIEvent
import com.example.takeahike.viewData.editRoute.EditRouteData
import com.example.takeahike.viewData.editRoute.SaveCompleteAction
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class EditRouteViewModel(mapQuestKey: String)
    : ViewModel(), ActionPresenter<SaveCompleteAction, EditRouteData> {

    override val action: MutableLiveData<ConsumableValue<SaveCompleteAction>> by lazy {
        MutableLiveData<ConsumableValue<SaveCompleteAction>>()
    }

    override val data: MutableLiveData<EditRouteData> by lazy {
        MutableLiveData<EditRouteData>()
    }

    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)
    private val dataParser : ParseRouteListData = ParseRouteListData()

    private var road : Road? = null
    private var wayPoints : List<GeoPoint> = listOf()

    init {
        // TODO: Make this configurable
        roadManager.addRequestOption("routeType=pedestrian")
    }

    // TODO: make calls async
    override fun update(event: Any) {
        if (event is SetWaypointsUIEvent) {
            setWaypoints(event)
        }
        else if (event is SaveEvent) {
            save(event.name)
        }
        else if (event is LoadRouteEvent && event.routeId != null) {
            loadRoute(event.routeId)
        }
    }

    private fun setWaypoints(event: SetWaypointsUIEvent) {
        val points = ArrayList(event.points)
        wayPoints = points
        val newPoint = event.newPoint
        if (newPoint != null) {
            points.add(newPoint)
        }

        data.value = if (points.count() > 1) {
            // TODO: Error handling for bad returns
            val path = roadManager.getRoad(points)
            road = path

            EditRouteData(
                points,
                path
            )
        }
        else {
            EditRouteData(
                points,
                Road()
            )
        }
    }

    private fun save(name: String) {
        road?.let {
            dataParser.saveData(name, it, wayPoints)
        } // TODO: Error handling if road == null

        action.value = ConsumableValue(SaveCompleteAction())
    }

    private fun loadRoute(routeId: String) {
        val route = dataParser.loadRoute(routeId)
        data.value = if (route != null) {
            val points = ArrayList(route.wayPoints.map { GeoPoint(it.lat, it.lon) })
            val road = if (points.count() > 1) {
                roadManager.getRoad(points)
            }
            else {
                Road()
            }
            EditRouteData(
                points,
                road
            )
        }
        else {
            //TODO: Error handling
            EditRouteData(
                arrayListOf(),
                Road()
            )
        }
    }
}