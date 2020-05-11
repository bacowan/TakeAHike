package com.example.takeahike.presenter

import com.example.takeahike.uiEvents.editRouteUIEvents.AddWaypointEvent
import com.example.takeahike.viewmodels.EditRouteViewModel
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class EditRoutePresenter(mapQuestKey: String) : Presenter<EditRouteViewModel> {

    private val _updateUI : InvokableEvent<EditRouteViewModel> = InvokableEvent()
    override val updateUI: Event<EditRouteViewModel>
        get() = _updateUI

    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)
    private val points : ArrayList<GeoPoint> = ArrayList()

    init {
        // TODO: Make this configurable
        roadManager.addRequestOption("routeType=pedestrian")
    }

    // TODO: make calls async
    override fun update(event: Any) {
        if (event is AddWaypointEvent) {
            addWaypoint(event)
        }
    }

    private fun addWaypoint(event: AddWaypointEvent) {
        if (event.index < 0) {
            points.add(GeoPoint(event.lat, event.lon))
        }
        else {
            points.add(event.index, GeoPoint(event.lat, event.lon))
        }

        val path = roadManager.getRoad(points)

        val viewModel = EditRouteViewModel(path)
        _updateUI.invoke(viewModel)
    }
}