package com.jsbonin.ethereumtracker

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jsbonin.ethereumtracker.repository.BinanceTickerRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import kotlinx.coroutines.MainScope

class EthereumTrackerApplication: Application(), LifecycleObserver {

    private val httpClient = HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 30000L
        }
    }

    private val applicationScope = MainScope()

    val binanceTickerRepository: BinanceTickerRepository by lazy {
        BinanceTickerRepository(httpClient, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
        binanceTickerRepository.connect()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onApplicationStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onApplicationResumed() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onApplicationStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onApplicationDestroyed() {

    }
}