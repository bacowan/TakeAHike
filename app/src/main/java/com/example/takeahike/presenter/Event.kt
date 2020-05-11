package com.example.takeahike.presenter

interface Event<TEventArgs> {
    fun subscribe(callback: (TEventArgs) -> Unit)
    fun unsubscribe(callback: (TEventArgs) -> Unit)
}