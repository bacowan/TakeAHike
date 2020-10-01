package com.example.takeahike.ui.adapters

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.takeahike.R

class RouteListItem(itemView: View, private val onItemClickListener: AdapterItemClickListener)
    : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var textView: TextView = itemView.findViewById(R.id.routeListText)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            onItemClickListener.onItemClick(v, adapterPosition)
        }
    }
}