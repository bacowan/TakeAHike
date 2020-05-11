package com.example.takeahike.presenter

import com.example.takeahike.uiEvents.editRouteUIEvents.SetWaypointsUIEvent
import com.example.takeahike.viewmodels.EditRouteViewModel
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class EditRoutePresenter(mapQuestKey: String) : Presenter<EditRouteViewModel> {

    private val _updateUI : InvokablePresenterEvent<EditRouteViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<EditRouteViewModel>
        get() = _updateUI

    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)

    init {
        // TODO: Make this configurable
        roadManager.addRequestOption("routeType=pedestrian")
    }

    // TODO: make calls async
    override fun update(event: Any) {
        if (event is SetWaypointsUIEvent) {
            setWaypoints(event)
        }
    }

    private fun setWaypoints(event: SetWaypointsUIEvent) {
        val points = ArrayList(event.points)
        val newPoint = event.newPoint
        if (newPoint != null) {
            points.add(newPoint)
        }

        val viewModel = if (points.count() > 1) {
            // TODO: Error handling for bad returns
            val path = roadManager.getRoad(points)

            EditRouteViewModel(
                points,
                path)
        }
        else {
            EditRouteViewModel(
                points,
                Road()
            )
        }

        _updateUI.invoke(viewModel)
    }
}