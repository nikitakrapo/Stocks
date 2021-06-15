package com.nikitakrapo.android.stocks.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikitakrapo.android.stocks.model.finnhub.MarketNewsArticle
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticle(marketNewsArticle: MarketNewsArticle)

    /**
     * @return certain category's sorted news (new -> old)
     */
    @Query("SELECT * FROM marketnewsarticle WHERE category = :marketNewsCategory ORDER BY datetime DESC")
    fun getNewsByCategory(marketNewsCategory: String): LiveData<List<MarketNewsArticle>>

    fun getNewsByCategory(marketNewsCategory: MarketNewsCategory): LiveData<List<MarketNewsArticle>>{
        return getNewsByCategory(marketNewsCategory.value)
    }

    @Query("DELETE FROM marketnewsarticle")
    fun deleteAllNews()

    @Query("SELECT * FROM marketnewsarticle")
    fun getAllNews(): LiveData<List<MarketNewsArticle>>
}