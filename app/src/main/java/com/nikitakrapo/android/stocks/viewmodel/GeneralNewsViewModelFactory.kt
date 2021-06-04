package com.nikitakrapo.android.stocks.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikitakrapo.android.stocks.repository.StockRepository

class GeneralNewsViewModelFactory( // TODO: maybe merge crypto and general news
        private val dataSource: StockRepository
        ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeneralNewsViewModel::class.java)) {
            return GeneralNewsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}