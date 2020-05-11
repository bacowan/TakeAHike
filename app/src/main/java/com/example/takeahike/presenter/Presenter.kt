package com.example.takeahike.presenter

interface Presenter<TViewModel> {
    val updateUI: Event<TViewModel>
    fun update(event: Any)
}