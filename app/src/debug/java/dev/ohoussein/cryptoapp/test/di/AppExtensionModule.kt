package dev.ohoussein.cryptoapp.test.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ohoussein.cryptoapp.config.DebuggableAppSetup
import dev.ohoussein.cryptoapp.config.IAppFlavorSetup
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppExtensionModule {

    @Provides
    @Singleton
    fun provideAppFlavorSetup(): IAppFlavorSetup = DebuggableAppSetup()
}
