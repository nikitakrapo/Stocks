package com.nikitakrapo.android.stocks.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nikitakrapo.android.stocks.domain.model.Stock
import com.nikitakrapo.android.stocks.domain.model.StockPortfolio
import com.nikitakrapo.android.stocks.network.response.MarketNewsArticle
import com.nikitakrapo.android.stocks.network.response.NewsArticleCategory

@Database(
    entities =
    [Stock::class,
        StockPortfolio::class,
        MarketNewsArticle::class,
        NewsArticleCategory::class],
    version = 6
)
@TypeConverters(Converters::class)
abstract class StockMarketDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun newsDao(): NewsDao
    abstract fun newsCategoriesDao(): NewsCategoriesDao
}