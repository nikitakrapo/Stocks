package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikitakrapo.android.stocks.repository.NewsRepository
import com.nikitakrapo.android.stocks.repository.StockRepository

class NewsViewModelFactory( // TODO: maybe merge crypto and general news
        private val dataSource: NewsRepository
        ) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}