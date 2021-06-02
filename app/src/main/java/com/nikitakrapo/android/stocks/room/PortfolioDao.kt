package com.nikitakrapo.android.stocks.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nikitakrapo.android.stocks.model.Stock
import com.nikitakrapo.android.stocks.model.StockPortfolio

@Dao
interface PortfolioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPortfolio(portfolio: StockPortfolio)

    @Query("DELETE FROM stockportfolio WHERE name = :name")
    fun deletePortfolio(name: String)

    @Query("SELECT * FROM stockportfolio WHERE name =:name")
    fun getPortfolio(name: String): LiveData<StockPortfolio>

    @Query("SELECT * FROM stock WHERE symbol IN (:stockSymbols)")
    fun getStocks(
            stockSymbols: List<String>
    ): LiveData<List<Stock>>

    @Query("DELETE FROM stockportfolio")
    fun deleteAllPortfolios()

}