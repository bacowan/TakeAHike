package com.example.takeahike.presenter

import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.currentHikeUIEvents.LoadRouteEvent
import com.example.takeahike.viewmodels.currentRoute.CurrentHikeViewModel
import com.example.takeahike.viewmodels.editRoute.EditRouteViewModel
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class CurrentHikePresenter(mapQuestKey: String) : Presenter<CurrentHikeViewModel> {
    private val _updateUI : InvokablePresenterEvent<CurrentHikeViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<CurrentHikeViewModel>
        get() = _updateUI

    private val roadManager : RoadManager = MapQuestRoadManager(mapQuestKey)
    private val dataParser : ParseRouteListData = ParseRouteListData()

    override fun update(event: Any) {
        if (event is LoadRouteEvent && event.routeId != null) {
            loadRoute(event.routeId)
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
            CurrentHikeViewModel(
                road
            )
        }
        else {
            //TODO: Error handling
            CurrentHikeViewModel(
                Road()
            )
        }

        // TODO: zoom in on the map
        _updateUI.invoke(viewModel)
    }
}