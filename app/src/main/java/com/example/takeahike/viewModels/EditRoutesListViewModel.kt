package com.example.takeahike.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewData.editRoute.EditRouteData
import com.example.takeahike.viewData.editRoute.SaveCompleteAction
import com.example.takeahike.viewData.editRouteList.EditRouteListData

class EditRoutesListViewModel
    : ViewModel(), Presenter<EditRouteListData> {
    override val data: MutableLiveData<EditRouteListData> by lazy {
        MutableLiveData<EditRouteListData>()
    }

    private val dataParser : ParseRouteListData = ParseRouteListData()

    override fun update(event: Any) {
        if (event is ListReadyUIEvent) {
            loadList()
        }
    }

    private fun loadList() {
        val routes = dataParser.loadData()
        data.value = EditRouteListData(routes)
    }
}