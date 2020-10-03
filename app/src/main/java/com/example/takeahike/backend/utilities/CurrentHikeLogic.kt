package com.example.takeahike.backend.utilities

import com.example.takeahike.backend.data.CurrentHike
import com.google.gson.Gson

class CurrentHikeLogic {
    private val gson = Gson()

    fun getCurrentHike(text: String): CurrentHike {
        return gson.fromJson(text, CurrentHike::class.java)
    }

    fun serializeCurrentHike(currentHike: CurrentHike): String {
        return gson.toJson(currentHike)
    }
}