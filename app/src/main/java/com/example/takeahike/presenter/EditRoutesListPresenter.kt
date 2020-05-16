package com.example.takeahike.presenter

import android.content.Context
import android.os.Parcel
import com.example.takeahike.backend.data.RouteSaveData
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewmodels.RouteListViewModel
import java.io.FileNotFoundException
import java.lang.Exception

class EditRoutesListPresenter : Presenter<RouteListViewModel> {
    private val _updateUI : InvokablePresenterEvent<RouteListViewModel> = InvokablePresenterEvent()
    override val updateUI: PresenterEvent<RouteListViewModel>
        get() = _updateUI

    private val dataParser : ParseRouteListData = ParseRouteListData()

    override fun update(event: Any) {
        if (event is ListReadyUIEvent) {
            loadList(event)
        }
    }

    private fun loadList(event: ListReadyUIEvent) {
        try {
            val data = dataParser.parseData(event.data)
            if (data != null) {
                _updateUI.invoke(RouteListViewModel(data.routes))
            }
            else {
                // TODO: Error handling
            }
        }
        catch (e : Exception) {
            // TODO: invalid save data
        }
    }
}