package dev.ohoussein.cryptoapp.data.network

import dev.ohoussein.cryptoapp.data.network.NetworkBuilder.createApiService
import dev.ohoussein.cryptoapp.data.network.NetworkBuilder.createRetrofit
import dev.ohoussein.cryptoapp.data.testutil.NetworkUtils.readMockFile
import dev.ohoussein.cryptoapp.data.testutil.NetworkUtils.withResponse
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ApiCoinGeckoServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: ApiCoinGeckoService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        service = createApiService(createRetrofit(baseUrl = mockWebServer.url("/")))
    }

    @Test
    fun getApiTopCryptoTest() = runBlocking {
        mockWebServer.withResponse(
            MockResponse()
                .setResponseCode(200)
                .setBody(readMockFile("mock_top_crypto_list.json"))
        )

        val response = service.getTopCrypto("")
        assertNotNull(response)
        assertEquals(100, response.size)

        val firstItem = response.first()

        assertEquals("bitcoin", firstItem.id)
        assertEquals("Bitcoin", firstItem.name)
        assertEquals(31827.0, firstItem.currentPrice, 0.0001)
        assertEquals("btc", firstItem.symbol)
    }

}