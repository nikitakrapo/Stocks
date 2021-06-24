package com.nikitakrapo.android.stocks.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nikitakrapo.android.stocks.model.Stock
import com.nikitakrapo.android.stocks.model.StockPortfolio
import com.nikitakrapo.android.stocks.model.finnhub.CompanyProfile2
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.NewsArticleCategory

@Database(
    entities =
        [Stock::class,
        StockPortfolio::class,
        MarketNewsArticle::class,
        NewsArticleCategory::class,
        CompanyProfile2::class],
    version = 5
)
@TypeConverters(Converters::class)
abstract class StockMarketDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun newsDao(): NewsDao
    abstract fun newsCategoriesDao(): NewsCategoriesDao
    abstract fun companyProfileDao(): CompanyProfileDao //TODO

    companion object {
        private var INSTANCE: StockMarketDatabase? = null

        fun getClient(context: Context): StockMarketDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                    .databaseBuilder(
                        context,
                        StockMarketDatabase::class.java,
                        "stock-market-database"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }

    }
}