package com.example.takeahike.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CurrentHikePresenterFactory(private val mapQuestKey: String, private val routeId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java, String::class.java)
            .newInstance(mapQuestKey, routeId)
    }
}