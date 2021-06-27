package com.nikitakrapo.android.stocks.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.nikitakrapo.android.stocks.network.response.CompanyProfile2
import com.nikitakrapo.android.stocks.network.response.StockPrice
import javax.inject.Inject

class Converters {

    @Inject
    lateinit var gson: Gson

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromJsonToList(json: String): List<String> {
        return gson.fromJson(json, List::class.java) as List<String>
    }

    @TypeConverter
    fun fromStockPriceToString(stockPrice: StockPrice): String {
        return gson.toJson(stockPrice)
    }

    @TypeConverter
    fun fromJsonToStockPrice(json: String): StockPrice {
        return gson.fromJson(json, StockPrice::class.java)
    }

    @TypeConverter
    fun fromCompanyProfile2ToJson(companyProfile2: CompanyProfile2): String {
        return gson.toJson(companyProfile2)
    }

    @TypeConverter
    fun fromJsonToCompanyProfile2(json: String): CompanyProfile2 {
        return gson.fromJson(json, CompanyProfile2::class.java)
    }

}