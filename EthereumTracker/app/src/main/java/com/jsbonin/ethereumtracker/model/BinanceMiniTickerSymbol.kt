package com.jsbonin.ethereumtracker.model

import com.google.gson.annotations.SerializedName

data class BinanceMiniTickerSymbol(
    @SerializedName("e") val eventType: String = "",
    @SerializedName("E") val eventTime: Long = 0L,
    @SerializedName("s") val symbol: String = "",
    @SerializedName("c") val closePrice: String = "",
    @SerializedName("o") val openPrice: String = "",
    @SerializedName("h") val highPrice: String = "",
    @SerializedName("l") val lowPrice: String = "",
    @SerializedName("v") val totalTradedBaseVolume: String = "",
    @SerializedName("q") val totalTradedQuoteVolume: String = ""
)