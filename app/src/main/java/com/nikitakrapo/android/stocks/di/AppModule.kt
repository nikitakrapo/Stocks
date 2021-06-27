package com.nikitakrapo.android.stocks.di

import android.content.Context
import com.nikitakrapo.android.stocks.presentation.StocksApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): StocksApplication {
        return app as StocksApplication
    }

}