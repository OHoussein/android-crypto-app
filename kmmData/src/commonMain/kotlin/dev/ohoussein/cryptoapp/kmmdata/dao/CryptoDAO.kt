package dev.ohoussein.cryptoapp.kmmdata.dao

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import dev.ohoussein.cryptoapp.domain.model.DomainCrypto
import dev.ohoussein.cryptoapp.kmmdata.CryptoDB
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CryptoDAO(
        private val database: CryptoDB,
        private val ioCoroutineDispatcher: CoroutineDispatcher,
) {

    suspend fun insert(cryptoList: List<DomainCrypto>) {
        withContext(ioCoroutineDispatcher) {
            database.cryptoQueries.transaction {
                cryptoList.forEach { crypto ->
                    database.cryptoQueries.insertItem(
                            id = crypto.id,
                            name = crypto.name,
                            imageUrl = crypto.imageUrl,
                            price = crypto.price,
                            symbol = crypto.symbol,
                            priceChangePercentIn24h = crypto.priceChangePercentIn24h,
                            orderInList = crypto.order.toLong(),
                    )
                }
            }
        }
    }

    fun selectAll(): Flow<List<DomainCrypto>> {
        database.cryptoQueries.selectAll()
        return database.cryptoQueries.selectAll { id, name, imageUrl, price, symbol, priceChangePercentIn24h, orderInList ->
            DomainCrypto(
                    id = id,
                    name = name,
                    imageUrl = imageUrl,
                    price = price,
                    symbol = symbol,
                    priceChangePercentIn24h = priceChangePercentIn24h,
                    order = orderInList.toInt(),
            )
        }
                .asFlow()
                .flowOn(ioCoroutineDispatcher)
                .mapToList()
    }
}
