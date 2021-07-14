package com.nikitakrapo.android.stocks.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.nikitakrapo.android.stocks.domain.model.NetworkResult
import com.nikitakrapo.android.stocks.data.network.response.SymbolLookup
import com.nikitakrapo.android.stocks.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _isRefreshing = MediatorLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _result = MediatorLiveData<NetworkResult<SymbolLookup>>()
    val result: LiveData<NetworkResult<SymbolLookup>> get() = _result

    private var symbolLookupJob: Job? = null

    private var queryJob: Job? = null
    private var previousQuery: String = ""

    fun searchNews(query: String, delayTimeMs: Int) {
        if (delayTimeMs < 0)
            throw IllegalArgumentException("Search delay should be positive")
        if (query == previousQuery || query == "")
            return
        queryJob?.cancel()
        queryJob = CoroutineScope(Dispatchers.IO).launch {
            delay(delayTimeMs.toLong())
            makeSymbolLookupCall(query)
            previousQuery = query
        }
    }

    private fun makeSymbolLookupCall(query: String) {
        _isRefreshing.postValue(true)
        symbolLookupJob = CoroutineScope(Dispatchers.IO).launch {
            val result =
                stockRepository.getSymbolLookup(query)
            _result.postValue(result)
            _isRefreshing.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        symbolLookupJob?.cancel()
        queryJob?.cancel()
    }

}