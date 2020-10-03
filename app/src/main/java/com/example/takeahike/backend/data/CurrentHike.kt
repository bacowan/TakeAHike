package com.example.takeahike.backend.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CurrentHike(
    var routeId: String,
    var percentComplete: Double
) : Parcelable