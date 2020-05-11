package com.example.takeahike.presenter

class InvokableEvent<TEventArgs> : Event<TEventArgs> {
    private val subscribers : HashSet<(TEventArgs) -> Unit> = HashSet()

    override fun subscribe(callback: (TEventArgs) -> Unit) {
        subscribers.add(callback)
    }

    override fun unsubscribe(callback: (TEventArgs) -> Unit) {
        subscribers.remove(callback)
    }

    fun invoke(eventArgs: TEventArgs) {
        subscribers.forEach {
            it(eventArgs)
        }
    }
}