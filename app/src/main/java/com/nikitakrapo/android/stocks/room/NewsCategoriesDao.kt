package com.nikitakrapo.android.stocks.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikitakrapo.android.stocks.model.finnhub.NewsArticleCategory
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory

@Dao
interface NewsCategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticleWithCategory(newsArticleCategory: NewsArticleCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticleWithCategory(newsArticleCategories: List<NewsArticleCategory>)

    @Query("SELECT id FROM newsarticlecategory WHERE category = :marketNewsCategory")
    fun getIdsByCategory(marketNewsCategory: String): List<Int>

    fun getIdsByCategory(marketNewsCategory: MarketNewsCategory): List<Int>{
        return getIdsByCategory(marketNewsCategory.value)
    }

    @Query("DELETE FROM newsarticlecategory WHERE category = :marketNewsCategory")
    fun deleteByCategory(marketNewsCategory: String)

    fun deleteByCategory(marketNewsCategory: MarketNewsCategory){
        deleteByCategory(marketNewsCategory.value)
    }

    @Query("DELETE FROM newsarticlecategory")
    fun deleteAllNewsArticleWithCategories()
}