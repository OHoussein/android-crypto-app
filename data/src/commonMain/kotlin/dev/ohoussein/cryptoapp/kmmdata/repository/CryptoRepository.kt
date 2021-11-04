package dev.ohoussein.cryptoapp.kmmdata.repository

import dev.ohoussein.cryptoapp.domain.model.DomainCrypto
import dev.ohoussein.cryptoapp.domain.model.DomainCryptoDetails
import dev.ohoussein.cryptoapp.domain.repo.ICryptoRepository
import dev.ohoussein.cryptoapp.kmmdata.dao.CryptoDAO
import dev.ohoussein.cryptoapp.kmmdata.mapper.DomainModelMapper
import dev.ohoussein.cryptoapp.kmmdata.network.CryptoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow

class CryptoRepository(
        private val service: CryptoApi,
        private val dao: CryptoDAO,
        private val mapper: DomainModelMapper,
) : ICryptoRepository {

    override fun getTopCryptoList(vsCurrency: String): Flow<List<DomainCrypto>> = dao.selectAll()
            .filter { it.isNotEmpty() }

    override suspend fun refreshTopCryptoList(vsCurrency: String) {
        val response = service.getTopCrypto(vsCurrency)
        dao.insert(mapper.convert(response))
    }

    override fun getCryptoDetails(cryptoId: String): Flow<DomainCryptoDetails> = flow {
        val response = service.getCryptoDetails(cryptoId)
        val domainData = mapper.convert(response)
        emit(domainData)
    }
}
