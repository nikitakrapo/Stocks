package com.nikitakrapo.android.stocks.repository

import android.content.Context
import com.nikitakrapo.android.stocks.room.StockMarketDatabase

class Repository(context: Context) {

    companion object {
        private lateinit var stockMarketDatabase: StockMarketDatabase

        private var repository: Repository? = null

        fun getInstance(context: Context): Repository{
            if (repository == null){
                repository = Repository(context)
                stockMarketDatabase = StockMarketDatabase.getClient(context)
            }
            return repository!!
        }
    }
}
