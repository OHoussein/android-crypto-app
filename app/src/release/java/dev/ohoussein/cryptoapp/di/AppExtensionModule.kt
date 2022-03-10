package dev.ohoussein.cryptoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohoussein.cryptoapp.config.IAppFlavorSetup
import dev.ohoussein.cryptoapp.config.ReleaseAppSetup
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppExtensionModule {

    @Provides
    @Singleton
    fun provideAppFlavorSetup(): IAppFlavorSetup = ReleaseAppSetup()
}
