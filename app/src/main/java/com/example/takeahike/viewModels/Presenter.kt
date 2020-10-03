package com.example.takeahike.viewModels

import androidx.lifecycle.MutableLiveData

interface Presenter<TData> {
    val data: MutableLiveData<TData>
    fun update(event: Any)
}