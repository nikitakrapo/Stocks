package com.nikitakrapo.android.stocks.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nikitakrapo.android.stocks.model.finnhub.NewsArticleWithCategory
import com.nikitakrapo.android.stocks.model.finnhub.enums.MarketNewsCategory

@Dao
interface NewsCategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticleWithCategory(newsArticleWithCategory: NewsArticleWithCategory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticleWithCategory(newsArticleWithCategories: List<NewsArticleWithCategory>)

    @Query("SELECT id FROM newsarticlewithcategory WHERE category = :marketNewsCategory")
    fun getIdsByCategory(marketNewsCategory: String): List<Int>

    fun getIdsByCategory(marketNewsCategory: MarketNewsCategory): List<Int>{
        return getIdsByCategory(marketNewsCategory.value)
    }

    @Query("DELETE FROM newsarticlewithcategory WHERE category = :marketNewsCategory")
    fun deleteByCategory(marketNewsCategory: String)

    fun deleteByCategory(marketNewsCategory: MarketNewsCategory){
        deleteByCategory(marketNewsCategory.value)
    }

    @Query("DELETE FROM newsarticlewithcategory")
    fun deleteAllNewsArticleWithCategories()
}