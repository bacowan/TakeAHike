package com.example.takeahike.uiEvents.currentHikeUIEvents

import android.location.Location
import com.google.android.gms.location.LocationResult

data class UpdatePositionEvent(val location: Location)