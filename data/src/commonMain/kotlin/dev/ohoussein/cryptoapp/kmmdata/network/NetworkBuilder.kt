package dev.ohoussein.cryptoapp.kmmdata.network

import dev.ohoussein.cryptoapp.kmmdata.initLogger
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*


object NetworkBuilder {

    const val TAG = "HTTP Client"

    fun client() = HttpClient {
        install(Logging) {
            level = LogLevel.INFO
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v(tag = TAG, message = message)
                }
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }.also {
            initLogger()
        }
    }
}