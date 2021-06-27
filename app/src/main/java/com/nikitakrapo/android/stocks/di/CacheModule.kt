package com.nikitakrapo.android.stocks.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.nikitakrapo.android.stocks.network.FinnhubApiService
import com.nikitakrapo.android.stocks.repository.NewsRepository
import com.nikitakrapo.android.stocks.repository.PortfolioRepository
import com.nikitakrapo.android.stocks.repository.StockRepository
import com.nikitakrapo.android.stocks.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideStockMarketDb(@ApplicationContext context: Context): StockMarketDatabase {
        return Room
            .databaseBuilder(
                context,
                StockMarketDatabase::class.java,
                "stock-market-database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStockDao(stockMarketDatabase: StockMarketDatabase): StockDao {
        return stockMarketDatabase.stockDao()
    }

    @Singleton
    @Provides
    fun providePortfolioDao(stockMarketDatabase: StockMarketDatabase): PortfolioDao {
        return stockMarketDatabase.portfolioDao()
    }

    @Singleton
    @Provides
    fun provideNewsDao(stockMarketDatabase: StockMarketDatabase): NewsDao {
        return stockMarketDatabase.newsDao()
    }

    @Singleton
    @Provides
    fun provideNewsCategoriesDao(stockMarketDatabase: StockMarketDatabase): NewsCategoriesDao {
        return stockMarketDatabase.newsCategoriesDao()
    }

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsDao: NewsDao,
        newsCategoriesDao: NewsCategoriesDao,
        finnhubApiService: FinnhubApiService
    ): NewsRepository {
        return NewsRepository(newsDao, newsCategoriesDao, finnhubApiService)
    }

    @Singleton
    @Provides
    fun providePortfolioRepository(
        portfolioDao: PortfolioDao,
        finnhubApiService: FinnhubApiService
    ): PortfolioRepository {
        return PortfolioRepository(portfolioDao, finnhubApiService)
    }

    @Singleton
    @Provides
    fun provideStockRepository(
        stockDao: StockDao,
        finnhubApiService: FinnhubApiService
    ): StockRepository {
        return StockRepository(stockDao, finnhubApiService)
    }

}