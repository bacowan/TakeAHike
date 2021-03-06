package com.example.takeahike.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditRoutePresenterFactory(private val mapQuestKey: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java)
            .newInstance(mapQuestKey)
    }
}