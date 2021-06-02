package com.nikitakrapo.android.stocks.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromStringList(list: List<String>): String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToList(json: String): List<String>{
        return Gson().fromJson(json, List::class.javaObjectType) as List<String>
    }

}