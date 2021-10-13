package com.jsbonin.ethereumtracker.usecase

import android.icu.text.NumberFormat
import com.jsbonin.ethereumtracker.model.PriceTrend
import com.jsbonin.ethereumtracker.repository.BinanceTickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class EthereumTickerUseCase(private val binanceTickerRepository: BinanceTickerRepository) {

    private val numberFormat = NumberFormat.getInstance().apply {
        minimumFractionDigits = 2
    }

    private var currentPrice: Double = 0.0

    private fun ethereumPriceUSDDouble(): Flow<Double> = binanceTickerRepository.ethMiniTickerFlow()
        .map { it.closePrice }
        .filter { it.isNotEmpty() }
        .map { closePrice ->
            closePrice.toDouble()
        }

    fun ethereumPriceUSD(): Flow<String> = ethereumPriceUSDDouble()
        .map { closePrice ->
            numberFormat.format(closePrice)
        }

    fun lastPriceChangeTrend(): Flow<PriceTrend> = ethereumPriceUSDDouble()
        .map { newPrice ->
            val trend = if (currentPrice > newPrice) PriceTrend.DOWN else PriceTrend.UP
            currentPrice = newPrice
            trend
        }
}
