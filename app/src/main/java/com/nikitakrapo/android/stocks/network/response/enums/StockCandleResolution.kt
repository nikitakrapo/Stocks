package com.nikitakrapo.android.stocks.network.response.enums

import com.google.gson.annotations.SerializedName

enum class StockCandleResolution(val value: String){
    @SerializedName("1")
    ONE_MINUTE("1"),
    @SerializedName("5")
    FIVE_MINUTES("5"),
    @SerializedName("15")
    FIFTEEN_MINUTES("15"),
    @SerializedName("30")
    THIRTY_MINUTES("30"),
    @SerializedName("60")
    SIXTY_MINUTES("60"),
    @SerializedName("D")
    DAY("D"),
    @SerializedName("W")
    WEEK("W"),
    @SerializedName("M")
    MONTH("M")
}