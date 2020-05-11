package com.example.takeahike.presenter

interface PresenterEvent<TEventArgs> {
    fun subscribe(callback: (TEventArgs) -> Unit)
    fun unsubscribe(callback: (TEventArgs) -> Unit)
}