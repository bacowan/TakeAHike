package com.example.takeahike.ui.edit.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takeahike.R
import com.example.takeahike.ui.adapters.RouteListAdapter
import com.example.takeahike.presenter.EditRoutesListPresenter
import com.example.takeahike.ui.adapters.AdapterItemClickListener
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewmodels.routeList.RouteListViewModel

class EditRoutesList : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val presenter : EditRoutesListPresenter = EditRoutesListPresenter()

    init {
        presenter.updateUI.subscribe { update(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_routes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(activity)

        val addButton: View = view.findViewById(R.id.fab)
        addButton.setOnClickListener {
            val action =
                EditRoutesListDirections.actionEditRoutesListToEditRoute()
            it.findNavController().navigate(action)
        }

        presenter.update(ListReadyUIEvent())
    }

    private fun update(viewModel : RouteListViewModel) {
        viewAdapter = RouteListAdapter(
            viewModel.routes.map { it.name }.toTypedArray(),
            object:AdapterItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val id = viewModel.routes[position].id
                    val action =
                        EditRoutesListDirections.actionEditRoutesListToEditRoute(id)
                    view.findNavController().navigate(action)
                }
            }
        )

        view?.let {
            recyclerView = it.findViewById<RecyclerView>(R.id.edit_list_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }
}