package com.nikitakrapo.android.stocks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikitakrapo.android.stocks.repository.StockRepository

class SearchViewModelFactory(
    private val stockRepository: StockRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(stockRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}