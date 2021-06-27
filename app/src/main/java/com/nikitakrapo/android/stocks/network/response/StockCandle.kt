package com.nikitakrapo.android.stocks.network.response

/**
 * Represents a stock prices candle
 * @property s status of the response. this field can either be ok or no_data
 * @property o list of open prices for returned candles
 * @property h list of open prices for returned candles
 * @property l list of low prices for returned candles
 * @property c list of close prices for returned candles
 * @property v list of volume data for returned candles
 * @property t list of timestamp for returned candles
 */
data class StockCandle(val s: String,
                       val o: List<Double>,
                       val h: List<Double>,
                       val l: List<Double>,
                       val c: List<Double>,
                       val v: List<Double>,
                       val t: List<Double>)