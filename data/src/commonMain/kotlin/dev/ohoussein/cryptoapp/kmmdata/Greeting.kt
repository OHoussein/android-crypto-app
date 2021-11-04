package dev.ohoussein.cryptoapp.kmmdata

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}