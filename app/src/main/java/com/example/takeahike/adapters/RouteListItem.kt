package com.example.takeahike.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.takeahike.R
import kotlinx.android.synthetic.main.route_list_item.view.*

class RouteListItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView: TextView = itemView.findViewById(R.id.textView2)

}