package com.nikitakrapo.android.stocks.data.network.response

import androidx.room.Entity
import androidx.room.PrimaryKey

/*For some reason "category" in Finnhub's News article response is not the same as MarketNewsCategory
so i decided to make separate DB with correspondences (MarketNewsArticle -> MarketNewsCategory)
*/
@Entity
data class NewsArticleCategory(
    @PrimaryKey
    val id: Int,
    val category: String
)