package com.nikitakrapo.android.stocks.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikitakrapo.android.stocks.model.Stock
import com.nikitakrapo.android.stocks.model.StockPortfolio

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPortfolio(portfolio: StockPortfolio)

    @Query("DELETE FROM stockportfolio WHERE name = :name")
    fun deletePortfolio(name: String)

    @Query("SELECT * FROM stock WHERE symbol IN (:stockSymbols)")
    suspend fun getStocks(
            stockSymbols: List<String>
    ): LiveData<List<LiveData<Stock>>>

}