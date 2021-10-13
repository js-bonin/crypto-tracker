package com.jsbonin.ethereumtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.jsbonin.ethereumtracker.ui.main.MainView
import com.jsbonin.ethereumtracker.usecase.EthereumTickerUseCase
import com.jsbonin.ethereumtracker.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory(
            application,
            EthereumTickerUseCase((application as EthereumTrackerApplication).binanceTickerRepository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView(viewModel)
        }
    }
}