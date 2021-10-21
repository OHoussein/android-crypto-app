package dev.ohoussein.cryptoapp.ui.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohoussein.cryptoapp.common.di.DIConstants.Qualifier.PERCENT_FORMATTER
import dev.ohoussein.cryptoapp.common.di.DIConstants.Qualifier.PRICE_FORMATTER
import java.text.NumberFormat
import java.util.*
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object UiCoreModule {

    @Provides
    @Named(PRICE_FORMATTER)
    fun providePriceFormatter(locale: Locale): NumberFormat =
        NumberFormat.getCurrencyInstance(locale)

    @Provides
    @Named(PERCENT_FORMATTER)
    fun providePercentFormatter(): NumberFormat = NumberFormat.getPercentInstance().apply {
        minimumFractionDigits = 2
    }
}
