package com.example.takeahike.presenter

import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewmodels.routeList.RouteListViewModel

class EditRoutesListPresenter : Presenter<RouteListViewModel> {
    private val _updateUI : InvokablePresenterEvent<RouteListViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<RouteListViewModel>
        get() = _updateUI

    private val dataParser : ParseRouteListData = ParseRouteListData()

    override fun update(event: Any) {
        if (event is ListReadyUIEvent) {
            loadList()
        }
    }

    private fun loadList() {
        val routes = dataParser.loadData()
        _updateUI.invoke(
            RouteListViewModel(
                routes
            )
        )
    }
}