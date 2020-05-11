package com.example.takeahike.presenter

interface Presenter<TViewModel> {
    val updateUI: PresenterEvent<TViewModel>
    fun update(event: Any)
}