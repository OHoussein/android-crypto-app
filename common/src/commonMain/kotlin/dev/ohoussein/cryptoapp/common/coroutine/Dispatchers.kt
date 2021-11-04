package dev.ohoussein.cryptoapp.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher

expect object Dispatchers {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}