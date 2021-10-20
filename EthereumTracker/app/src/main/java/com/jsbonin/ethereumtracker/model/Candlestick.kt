package com.jsbonin.ethereumtracker.model

data class Candlestick(
    val openPrice: Double = 0.0,
    val closePrice: Double = 0.0,
    val highPrice: Double = 0.0,
    val lowPrice: Double = 0.0
)
