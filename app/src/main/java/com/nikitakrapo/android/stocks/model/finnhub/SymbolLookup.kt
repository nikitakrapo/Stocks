package com.nikitakrapo.android.stocks.model.finnhub

/**
 * Represents a symbol lookup response
 * @property count number of results
 * @property result array of search results
 */
data class SymbolLookup(val count: Int,
                        val result: List<SingleLookupResponse>){
    /**
     * Represents an element in symbol lookup response
     * DO NOT USE ANYWHERE FROM symbolLookup!!
     * @property description symbol description
     * @property displaySymbol display symbol name
     * @property symbol unique symbol used to identify this symbol used in /stock/candle endpoint
     * @property type security type
     */
    data class SingleLookupResponse(
        val description: String?,
        val displaySymbol: String?,
        val symbol: String?,
        val type: String?)
}