package com.nikitakrapo.android.stocks.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nikitakrapo.android.stocks.model.Stock
import com.nikitakrapo.android.stocks.model.StockPortfolio

@Database(entities = [Stock::class, StockPortfolio::class], version = 1)
@TypeConverters(Converters::class)
abstract class StockMarketDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
    abstract fun portfolioDao(): PortfolioDao

    companion object {
        private var INSTANCE: StockMarketDatabase? = null

        fun getClient(context: Context) : StockMarketDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(context, StockMarketDatabase::class.java, "stock-market-database")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return INSTANCE!!
        }

    }
}