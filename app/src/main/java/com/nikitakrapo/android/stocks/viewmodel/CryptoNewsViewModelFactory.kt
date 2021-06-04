package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikitakrapo.android.stocks.repository.StockRepository

class CryptoNewsViewModelFactory( // TODO: maybe merge crypto and general news
    private val dataSource: StockRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptoNewsViewModel::class.java)) {
            return CryptoNewsViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}