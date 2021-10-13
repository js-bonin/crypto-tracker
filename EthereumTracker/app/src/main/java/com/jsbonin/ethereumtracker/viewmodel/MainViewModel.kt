package com.jsbonin.ethereumtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jsbonin.ethereumtracker.repository.BinanceTickerRepository
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlin.math.round

class MainViewModel(application: Application, private val binanceTickerRepository: BinanceTickerRepository) : AndroidViewModel(application) {

    fun ethereumPriceUSD() = binanceTickerRepository.ethMiniTickerFlow()
        .map { it.closePrice }
        .filter { it.isNotEmpty() }
        .map { closePrice ->
            val closePriceDouble = round(closePrice.toDouble() * 100) / 100
            closePriceDouble.toString()
        }

    class Factory(private val application: Application, private val binanceTickerRepository: BinanceTickerRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application, binanceTickerRepository) as T
        }
    }
}