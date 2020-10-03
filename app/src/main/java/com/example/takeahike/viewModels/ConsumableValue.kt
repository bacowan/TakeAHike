package com.example.takeahike.viewModels

// see https://stackoverflow.com/a/59058262/6043528
class ConsumableValue<T>(private val data: T) {
    private var consumed = false

    fun handle(action: (T) -> Unit) {
        val wasConsumed = consumed
        consumed = true
        if (!wasConsumed) {
            action(data)
        }
    }
}