package com.example.takeahike.presenter

import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewmodels.editRouteList.EditRouteListViewModel
import com.example.takeahike.viewmodels.selectRoute.SelectRouteViewModel

class SelectRoutePresenter : Presenter<SelectRouteViewModel> {
    private val _updateUI : InvokablePresenterEvent<SelectRouteViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<SelectRouteViewModel>
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
            SelectRouteViewModel(
                routes
            )
        )
    }
}