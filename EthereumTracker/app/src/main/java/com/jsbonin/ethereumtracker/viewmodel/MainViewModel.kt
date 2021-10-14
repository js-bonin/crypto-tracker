package com.jsbonin.ethereumtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jsbonin.ethereumtracker.model.PriceTrend
import com.jsbonin.ethereumtracker.ui.theme.downColor
import com.jsbonin.ethereumtracker.ui.theme.upColor
import com.jsbonin.ethereumtracker.usecase.EthereumTickerUseCase
import kotlinx.coroutines.flow.map

class MainViewModel(application: Application, private val ethereumTickerUseCase: EthereumTickerUseCase) : AndroidViewModel(application) {

    fun ethereumPriceUSD() = ethereumTickerUseCase.ethereumPriceUSD()

    fun ethereumPriceColor() = ethereumTickerUseCase.lastPriceChangeTrend().map { trend ->
        when (trend) {
            PriceTrend.UP -> upColor
            PriceTrend.DOWN -> downColor
        }
    }

    class Factory(private val application: Application, private val ethereumTickerUseCase: EthereumTickerUseCase) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application, ethereumTickerUseCase) as T
        }
    }
}