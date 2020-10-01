package com.example.takeahike.ui.hike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.takeahike.R
import com.example.takeahike.backend.utilities.CurrentHikeLogic

class NoCurrentHike : Fragment() {

    private val currentHikeLogic : CurrentHikeLogic = CurrentHikeLogic()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.no_current_hike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (currentHikeLogic.currentRouteExists()) {
            val action =
                NoCurrentHikeDirections.actionNoCurrentHikeToCurrentHike()
            view.findNavController().navigate(action)
        }
        else {
            val button = view.findViewById<Button>(R.id.new_hike_button)
            button.setOnClickListener {
                val action =
                    NoCurrentHikeDirections.actionSelectHikeToSelectRouteList()
                view.findNavController().navigate(action)
            }
        }
    }
}