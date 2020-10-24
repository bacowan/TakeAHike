package com.example.takeahike.viewModels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.takeahike.viewModels.CurrentHikeViewModel

class CurrentHikePresenterFactory(
        private val application: Application,
        private val mapQuestKey: String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentHikeViewModel(application, mapQuestKey) as T
    }
}