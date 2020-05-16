package com.example.takeahike.presenter

import com.example.takeahike.backend.data.RouteSaveData
import com.example.takeahike.backend.data.RouteSaveItem
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.editRouteUIEvents.SetWaypointsUIEvent
import com.example.takeahike.viewmodels.EditRouteViewModel
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import java.io.FileOutputStream

class EditRoutePresenter(mapQuestKey: String) : Presenter<EditRouteViewModel> {

    private val _updateUI : InvokablePresenterEvent<EditRouteViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<EditRouteViewModel>
        get() = _updateUI

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

    fun saveNew(name: String, fos: FileOutputStream?, existingData: ByteArray?) {
        val existingList = if (existingData != null) {
            dataParser.parseData(existingData) ?: RouteSaveData(mutableListOf())
        }
        else {
            RouteSaveData(mutableListOf())
        }


        val theRoad = road
        if (theRoad != null) {
            existingList.routes.add(RouteSaveItem(
                name,
                theRoad,
                wayPoints
            ))
            dataParser.writeData(fos, existingList)
        }
        else {
            // TODO: Error handling
        }
    }
}