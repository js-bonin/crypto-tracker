package com.jsbonin.ethereumtracker

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.jsbonin.ethereumtracker.repository.BinanceTickerRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import kotlinx.coroutines.MainScope

class EthereumTrackerApplication: Application(), LifecycleObserver {

    private val applicationScope = MainScope()

    private val httpClient = HttpClient(CIO) {
        install(WebSockets) {
            pingInterval = 30000L
        }
    }

    val binanceTickerRepository: BinanceTickerRepository by lazy {
        BinanceTickerRepository(httpClient, applicationScope)
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onApplicationStart() {
        binanceTickerRepository.connect()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onApplicationResumed() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onApplicationStop() {
        binanceTickerRepository.close()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onApplicationDestroyed() {
        binanceTickerRepository.close()
    }
}