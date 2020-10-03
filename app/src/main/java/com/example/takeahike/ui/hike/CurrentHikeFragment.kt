package com.example.takeahike.ui.hike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.takeahike.R
import com.example.takeahike.viewModels.CurrentHikeViewModel
import com.example.takeahike.ui.edit.editor.ClickOverlay
import com.example.takeahike.ui.edit.editor.EditRoute
import com.example.takeahike.uiEvents.currentHikeUIEvents.LoadRouteEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.currentRoute.RecenterAction
import com.example.takeahike.viewData.editRoute.EditRouteData
import com.example.takeahike.viewData.editRoute.SaveCompleteAction
import com.example.takeahike.viewModels.ActionPresenter
import com.example.takeahike.viewModels.EditRouteViewModel
import com.example.takeahike.viewModels.factories.CurrentHikePresenterFactory
import com.example.takeahike.viewModels.factories.EditRoutePresenterFactory
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class CurrentHikeFragment : Fragment() {
    private lateinit var map: MapView
    private val args: CurrentHikeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = getViewModel()
        viewModel.data.observe(this, Observer { update(it) })
        viewModel.action.observe(this, Observer { it.handle { value -> updateAction(value) }})
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.current_hike, container, false)

        map = view.findViewById(R.id.current_hike_map)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                map.controller.setZoom(getDouble(currentZoomKey))
                map.controller.setCenter(GeoPoint(
                    getDouble(currentLatKey),
                    getDouble(currentLonKey)
                ))
            }
        }
        else {
            map.controller.setZoom(3.0)
        }

        return view
    }

    private fun update(viewmodel : CurrentHikeData) {
        map.overlays.removeIf { it !is ClickOverlay }

        val roadOverlay = RoadManager.buildRoadOverlay(viewmodel.road)
        map.overlays.add(roadOverlay)

        map.invalidate()
    }

    private fun updateAction(action : RecenterAction) {
        map.controller.setCenter(GeoPoint(action.lat, action.lon))
        map.controller.setZoom(18.0)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble(currentZoomKey, map.zoomLevelDouble)
        outState.putDouble(currentLatKey, map.mapCenter.latitude)
        outState.putDouble(currentLonKey, map.mapCenter.longitude)
        super.onSaveInstanceState(outState)
    }

    private fun getViewModel() : ActionPresenter<RecenterAction, CurrentHikeData> {
        return ViewModelProvider(this, CurrentHikePresenterFactory(resources.getString(R.string.map_quest_key), args.currentHike.routeId)).get(
            CurrentHikeViewModel::class.java)
    }

    private companion object Constants {
        const val currentZoomKey = "currentZoom"
        const val currentLatKey = "currentLat"
        const val currentLonKey = "currentLon"
    }
}