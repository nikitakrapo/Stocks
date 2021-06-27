package com.nikitakrapo.android.stocks.network.response

/**
 * Represents a symbol lookup response
 * @property count number of results
 * @property result array of search results
 */
data class SymbolLookup(val count: Int,
                        val result: List<SingleSymbolLookup>){
    /**
     * Represents an element in symbol lookup response
     * @property description symbol description
     * @property displaySymbol display symbol name
     * @property symbol unique symbol used to identify this symbol used in /stock/candle endpoint
     * @property type security type
     */
    data class SingleSymbolLookup(
        val description: String,
        val displaySymbol: String,
        val symbol: String,
        val type: String)
}