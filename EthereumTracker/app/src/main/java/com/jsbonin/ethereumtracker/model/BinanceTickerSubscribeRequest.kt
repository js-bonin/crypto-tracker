package com.jsbonin.ethereumtracker.model

data class BinanceTickerSubscribeRequest(
    val method: String,
    val params: List<String>,
    val id: Int
)