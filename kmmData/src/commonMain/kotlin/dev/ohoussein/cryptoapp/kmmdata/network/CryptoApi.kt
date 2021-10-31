package dev.ohoussein.cryptoapp.kmmdata.network

import dev.ohoussein.cryptoapp.kmmdata.model.CryptoDetailsResponse
import dev.ohoussein.cryptoapp.kmmdata.model.TopCryptoResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class CryptoApi(
        private val httpClient: HttpClient = NetworkBuilder.client(),
        private val baseUrl: String = "api.coingecko.com/api",
) {

    companion object {
        fun create() = CryptoApi()
    }

    suspend fun getTopCrypto(vsCurrency: String): List<TopCryptoResponse> = httpClient.request {
        url {
            protocol = URLProtocol.HTTPS
            host = baseUrl
            path("v3/coins/markets")
            formData {
                parameter("vs_currency", vsCurrency)
            }
        }
    }

    suspend fun getCryptoDetails(cryptoId: String): CryptoDetailsResponse = httpClient.get() {
        url {
            protocol = URLProtocol.HTTPS
            host = baseUrl
            path("v3/coins/$cryptoId")
            formData {
                parameter("tickers", false)
                parameter("market_data", false)
                parameter("community_data", false)
                parameter("developer_data", false)
                parameter("sparkline", false)
            }
        }
    }


}