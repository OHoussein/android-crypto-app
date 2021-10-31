package dev.ohoussein.cryptoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohoussein.cryptoapp.domain.repo.ICryptoRepository
import dev.ohoussein.cryptoapp.kmmdata.dao.CryptoDAO
import dev.ohoussein.cryptoapp.kmmdata.mapper.DomainModelMapper
import dev.ohoussein.cryptoapp.kmmdata.network.CryptoApi
import dev.ohoussein.cryptoapp.kmmdata.repository.CryptoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataRepoModule {

    @Provides
    @Singleton
    fun provideCryptoRepository(
            service: CryptoApi,
            database: CryptoDAO,
            mapper: DomainModelMapper
    ): ICryptoRepository =
            CryptoRepository(service, database, mapper)
}
