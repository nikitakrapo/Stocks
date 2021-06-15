package com.nikitakrapo.android.stocks.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nikitakrapo.android.stocks.model.Stock

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addStock(stock: Stock)

    @Query("SELECT * FROM stock WHERE symbol=:symbol")
    fun getStock(symbol: String): LiveData<Stock>

    @Query("DELETE FROM stock WHERE symbol=:symbol")
    fun deleteStock(symbol: String)

}