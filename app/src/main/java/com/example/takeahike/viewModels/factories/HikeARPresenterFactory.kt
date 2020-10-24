package com.example.takeahike.viewModels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.takeahike.viewModels.CurrentHikeViewModel
import com.example.takeahike.viewModels.HikeARViewModel

class HikeARPresenterFactory(
        private val application: Application,
        private val mapQuestKey: String) :
        ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HikeARViewModel(application, mapQuestKey) as T
    }
}