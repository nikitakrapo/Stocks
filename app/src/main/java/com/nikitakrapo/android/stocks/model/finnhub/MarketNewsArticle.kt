package com.nikitakrapo.android.stocks.model.finnhub

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a market news article
 * @property category News category.
 * @property datetime Published time in UNIX timestamp.
 * @property headline News headline.
 * @property id News ID. This value can be used for minId params to get the latest news only.
 * @property image Thumbnail image URL.
 * @property related Related stocks and companies mentioned in the article.
 * @property source News source.
 * @property summary News summary.
 * @property url URL of the original article.
 */
@Entity
data class MarketNewsArticle(val category: String?,
                             val datetime: Long?,
                             val headline: String?,
                             @PrimaryKey
                             val id: Int?,
                             val image: String?,
                             val related: String?,
                             val source: String?,
                             val summary: String?,
                             val url: String?)
