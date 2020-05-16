package com.example.takeahike.presenter

import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.editRouteUIEvents.SaveEvent
import com.example.takeahike.uiEvents.editRouteUIEvents.SetWaypointsUIEvent
import com.example.takeahike.viewmodels.editRoute.EditRouteViewModel
import com.example.takeahike.viewmodels.editRoute.SaveCompleteAction
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class EditRoutePresenter(mapQuestKey: String) : ActionPresenter<SaveCompleteAction, EditRouteViewModel> {

    private val _updateUI : InvokablePresenterEvent<EditRouteViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<EditRouteViewModel>
        get() = _updateUI

    private val _updateUIAction : InvokablePresenterEvent<SaveCompleteAction> = InvokablePresenterEvent()
    override val updateUIAction: PresenterEvent<SaveCompleteAction>
        get() = _updateUIAction

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
    }

    private fun setWaypoints(event: SetWaypointsUIEvent) {
        val points = ArrayList(event.points)
        wayPoints = points
        val newPoint = event.newPoint
        if (newPoint != null) {
            points.add(newPoint)
        }

        val viewModel = if (points.count() > 1) {
            // TODO: Error handling for bad returns
            val path = roadManager.getRoad(points)
            road = path

            EditRouteViewModel(
                points,
                path
            )
        }
        else {
            EditRouteViewModel(
                points,
                Road()
            )
        }

        _updateUI.invoke(viewModel)
    }

    private fun save(name: String) {
        road?.let {
            dataParser.saveData(name, it, wayPoints)
        } // TODO: Error handling if road == null
        _updateUIAction.invoke(SaveCompleteAction())
    }
}