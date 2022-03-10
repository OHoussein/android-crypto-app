package dev.ohoussein.cryptoapp.test.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohoussein.cryptoapp.common.coroutine.CoroutineContextProvider
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProvider()

    @Provides
    fun provideLocale(): Locale = Locale.US
}
