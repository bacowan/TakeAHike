package com.example.takeahike.ui.hike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.takeahike.R
import com.example.takeahike.backend.data.LatLng
import com.example.takeahike.ui.LocationUpdater
import com.example.takeahike.uiEvents.hikeAR.UIReadyEvent
import com.example.takeahike.uiEvents.hikeAR.UpdatePositionEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.currentRoute.RecenterAction
import com.example.takeahike.viewData.hikeAR.HikeARData
import com.example.takeahike.viewModels.ActionPresenter
import com.example.takeahike.viewModels.CurrentHikeViewModel
import com.example.takeahike.viewModels.HikeARViewModel
import com.example.takeahike.viewModels.Presenter
import com.example.takeahike.viewModels.factories.CurrentHikePresenterFactory
import com.example.takeahike.viewModels.factories.HikeARPresenterFactory
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class HikeAR : Fragment() {

    private lateinit var locationUpdater: LocationUpdater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hike_ar, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let { locationUpdater = LocationUpdater(
            LocationServices.getFusedLocationProviderClient(it),
            locationUpdateCallback) }

        getViewModel().data.observe(this, { update(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = view.findViewById<WebView>(R.id.ar_web_view)
        webView.settings.javaScriptEnabled = true
        webView?.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url?.toString())
                return false
            }
        }

        getViewModel().update(UIReadyEvent())
    }

    private fun update(hikeARData: HikeARData) {
        val webView = view?.findViewById<WebView>(R.id.ar_web_view)
        webView?.loadUrl(
            "https://maps.google.com/maps?layer=c&cbll=${hikeARData.latLng.lat},${hikeARData.latLng.lon}")
    }

    private fun getViewModel() : Presenter<HikeARData> {
        return ViewModelProvider(
            this,
            HikeARPresenterFactory(
                activity?.application!!,
                resources.getString(R.string.map_quest_key))
            )
            .get(HikeARViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        locationUpdater.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        locationUpdater.endLocationUpdates()
    }

    private val locationUpdateCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult?) {
            val viewModel = getViewModel()
            if (location != null) {
                viewModel.update(
                    UpdatePositionEvent(
                        LatLng(
                            location.lastLocation.latitude,
                            location.lastLocation.longitude
                        )
                    )
                )
            }
        }
    }

}