package com.example.takeahike.presenter

import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.currentHikeUIEvents.LoadRouteEvent
import com.example.takeahike.viewmodels.currentRoute.CurrentHikeViewModel
import com.example.takeahike.viewmodels.currentRoute.RecenterAction
import com.example.takeahike.viewmodels.editRoute.EditRouteViewModel
import com.example.takeahike.viewmodels.editRoute.SaveCompleteAction
import org.osmdroid.bonuspack.routing.MapQuestRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class CurrentHikePresenter(mapQuestKey: String) : ActionPresenter<RecenterAction, CurrentHikeViewModel> {
    private val _updateUI : InvokablePresenterEvent<CurrentHikeViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<CurrentHikeViewModel>
        get() = _updateUI

    private val _updateUIAction : InvokablePresenterEvent<RecenterAction> = InvokablePresenterEvent()
    override val updateUIAction: PresenterEvent<RecenterAction>
        get() = _updateUIAction

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
                points,
                road
            )
        }
        else {
            //TODO: Error handling
            CurrentHikeViewModel(
                listOf(),
                Road()
            )
        }

        val recenterAction = route?.wayPoints?.firstOrNull()?.let {
            RecenterAction(it.lat, it.lon)
        }

        _updateUI.invoke(viewModel)

        if (recenterAction != null) {
            _updateUIAction.invoke(recenterAction)
        }
    }
}