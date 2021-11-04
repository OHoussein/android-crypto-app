package dev.ohoussein.cryptoapp.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher

open class CoroutineContextProvider {
    open val io: CoroutineDispatcher by lazy { Dispatchers.io }
    open val main: CoroutineDispatcher by lazy { Dispatchers.main }
}
