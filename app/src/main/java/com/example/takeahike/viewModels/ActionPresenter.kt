package com.example.takeahike.viewModels

import androidx.lifecycle.MutableLiveData

interface ActionPresenter<TAction, TViewModel> : Presenter<TViewModel> {
    val action : MutableLiveData<ConsumableValue<TAction>>
}