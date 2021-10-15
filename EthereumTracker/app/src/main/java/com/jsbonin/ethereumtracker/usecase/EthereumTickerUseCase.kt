package com.jsbonin.ethereumtracker.usecase

import android.icu.text.NumberFormat
import com.jsbonin.ethereumtracker.model.PriceTrend
import com.jsbonin.ethereumtracker.repository.BinanceTickerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class EthereumTickerUseCase(private val binanceTickerRepository: BinanceTickerRepository) {

    private val numberFormat2digits = NumberFormat.getInstance().apply {
        minimumFractionDigits = 2
    }

    private val numberFormat6digits = NumberFormat.getInstance().apply {
        minimumFractionDigits = 6
    }

    private val percentFormat = NumberFormat.getPercentInstance().apply {
        minimumFractionDigits = 2
    }

    private var currentPriceUSDT: Double = 0.0
    private var currentPriceBTC: Double = 0.0

    //USDT
    private fun priceUSDDouble(): Flow<Double> = binanceTickerRepository.miniTickerETHUSDTFlow()
        .map { it.closePrice }
        .filter { it.isNotEmpty() }
        .map { closePrice ->
            closePrice.toDouble()
        }

    fun priceUSD(): Flow<String> = priceUSDDouble()
        .map { closePrice ->
            numberFormat2digits.format(closePrice)
        }

    fun lastPriceUSDChangeTrend(): Flow<PriceTrend> = priceUSDDouble()
        .map { newPrice ->
            val trend = if (currentPriceUSDT > newPrice) PriceTrend.DOWN else PriceTrend.UP
            currentPriceUSDT = newPrice
            trend
        }

    private fun priceUSDPercentChange24hourDouble(): Flow<Double> = binanceTickerRepository.miniTickerETHUSDTFlow()
        .filter { it.openPrice.isNotEmpty() && it.closePrice.isNotEmpty() }
        .map {
            val openPrice = it.openPrice.toDouble()
            val closePrice = it.closePrice.toDouble()
            val increase = closePrice - openPrice
            val percentIncrease = (increase / openPrice)
            percentIncrease
        }

    fun priceUSDPercentChange24hour(): Flow<String> = priceUSDPercentChange24hourDouble()
        .map { percentFormat.format(it) }

    fun priceUSDPercentChangeTrend(): Flow<PriceTrend> = priceUSDPercentChange24hourDouble()
        .map {
            if (it > 0) PriceTrend.UP else PriceTrend.DOWN
        }

    //BTC
    private fun priceBTCDouble(): Flow<Double> = binanceTickerRepository.miniTickerETHBTCFlow()
        .map { it.closePrice }
        .filter { it.isNotEmpty() }
        .map { closePrice ->
            closePrice.toDouble()
        }

    fun priceBTC(): Flow<String> = priceBTCDouble()
        .map { closePrice ->
            numberFormat6digits.format(closePrice)
        }

    fun lastPriceBTCChangeTrend(): Flow<PriceTrend> = priceBTCDouble()
        .map { newPrice ->
            val trend = if (currentPriceBTC > newPrice) PriceTrend.DOWN else PriceTrend.UP
            currentPriceBTC = newPrice
            trend
        }

    private fun priceBTCPercentChange24hourDouble(): Flow<Double> = binanceTickerRepository.miniTickerETHBTCFlow()
        .filter { it.openPrice.isNotEmpty() && it.closePrice.isNotEmpty() }
        .map {
            val openPrice = it.openPrice.toDouble()
            val closePrice = it.closePrice.toDouble()
            val increase = closePrice - openPrice
            val percentIncrease = (increase / openPrice)
            percentIncrease
        }

    fun priceBTCPercentChange24hour(): Flow<String> = priceBTCPercentChange24hourDouble()
        .map { percentFormat.format(it) }

    fun priceBTCPercentChangeTrend(): Flow<PriceTrend> = priceBTCPercentChange24hourDouble()
        .map {
            if (it > 0) PriceTrend.UP else PriceTrend.DOWN
        }
}
