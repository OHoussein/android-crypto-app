package dev.ohoussein.cryptoapp.test.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ohoussein.cryptoapp.common.coroutine.CoroutineContextProvider
import dev.ohoussein.cryptoapp.kmmdata.CryptoDB
import dev.ohoussein.cryptoapp.kmmdata.dao.CryptoDAO
import dev.ohoussein.cryptoapp.kmmdata.initDatabase
import dev.ohoussein.cryptoapp.kmmdata.mapper.DomainModelMapper
import dev.ohoussein.cryptoapp.kmmdata.network.CryptoApi
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CryptoDB = initDatabase(context)

    @Provides
    @Singleton
    fun provideCryptoDAO(db: CryptoDB, coroutineContextProvider: CoroutineContextProvider): CryptoDAO =
            CryptoDAO(db, coroutineContextProvider.io)

    @Provides
    @Singleton
    fun provideCryptoAPI(): CryptoApi = CryptoApi.create()

    @Provides
    fun provideDomainModelMapper(locale: Locale): DomainModelMapper = DomainModelMapper(locale.language)
}
