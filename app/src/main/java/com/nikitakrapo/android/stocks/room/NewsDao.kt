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
    fun addNews(marketNewsArticle: MarketNewsArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNews(marketNewsArticles: List<MarketNewsArticle>)

    @Query("SELECT * FROM marketnewsarticle WHERE id IN (:ids) ORDER BY datetime DESC")
    fun getNewsByIdsOrdered(ids: List<Int>): List<MarketNewsArticle>

    @Query("DELETE FROM marketnewsarticle")
    fun deleteAllNews()

    @Query("DELETE FROM marketnewsarticle WHERE id IN (:ids)")
    fun deleteNewsByIds(ids: List<Int>)

    @Query("SELECT * FROM marketnewsarticle")
    fun getAllNews(): List<MarketNewsArticle>
}