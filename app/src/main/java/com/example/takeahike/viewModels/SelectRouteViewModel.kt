package com.example.takeahike.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.takeahike.R
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.backend.utilities.ParseRouteListData
import com.example.takeahike.backend.utilities.saveCurrentHike
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.uiEvents.routeListUIEvents.StartNewHikeUIEvent
import com.example.takeahike.viewData.editRoute.EditRouteData
import com.example.takeahike.viewData.editRoute.SaveCompleteAction
import com.example.takeahike.viewData.selectRoute.SelectRouteData

class SelectRouteViewModel(application: Application)
    : AndroidViewModel(application), Presenter<SelectRouteData> {
    override val data: MutableLiveData<SelectRouteData> by lazy {
        MutableLiveData<SelectRouteData>()
    }

    private val dataParser : ParseRouteListData = ParseRouteListData()

    override fun update(event: Any) {
        if (event is ListReadyUIEvent) {
            loadList()
        }
        else if (event is StartNewHikeUIEvent) {
            overwriteCurrentHike(event.routeId)
        }
    }

    private fun overwriteCurrentHike(routeId: String) {
        val applicationContext = getApplication<Application>().applicationContext
        saveCurrentHike(
            CurrentHike(routeId, null, 0.0),
            applicationContext,
            applicationContext.getString(R.string.current_hike_file_name)
        )
    }

    private fun loadList() {
        val routes = dataParser.loadData()
        data.value = SelectRouteData(routes)
    }
}