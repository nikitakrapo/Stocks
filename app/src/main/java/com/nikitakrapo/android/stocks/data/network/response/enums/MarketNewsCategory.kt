package com.nikitakrapo.android.stocks.data.network.response.enums

import com.google.gson.annotations.SerializedName

enum class MarketNewsCategory(val value: String){
    @SerializedName("general")
    GENERAL("general"),
    @SerializedName("forex")
    FOREX("forex"),
    @SerializedName("crypto")
    CRYPTO("crypto"),
    @SerializedName("merger")
    MERGER("merger")
}