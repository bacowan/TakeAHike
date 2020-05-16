package com.example.takeahike.backend.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.util.GeoPoint

@Parcelize
data class RouteSaveItem(
    val name : String,
    val road : Road,
    val wayPoints : List<GeoPoint>
) : Parcelable

@Parcelize
data class RouteSaveData(
    val routes : MutableList<RouteSaveItem>
) : Parcelable