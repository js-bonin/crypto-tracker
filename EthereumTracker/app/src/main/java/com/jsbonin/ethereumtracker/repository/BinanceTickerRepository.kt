package com.jsbonin.ethereumtracker.repository

import android.util.Log
import com.google.gson.Gson
import com.jsbonin.ethereumtracker.model.BinanceTickerSubscribeRequest
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BinanceTickerRepository(private val httpClient: HttpClient, private val applicationScope: CoroutineScope) {

    companion object {
        const val BINANCE_WEBSOCKET_MINITICKER_URL = "wss://stream.binance.com:9443/ws/ethusdt@miniTicker"
        const val MINI_TICKER_ETHUSD = "ethusdt@miniTicker"
        const val TICKER_ETHUSD = "ethusd@ticker"
    }

    private val gson = Gson()

    private val subscribeRequest = BinanceTickerSubscribeRequest(
        "SUBSCRIBE",
        params = listOf(MINI_TICKER_ETHUSD),
        id = 1
    )

    fun connect() {
        applicationScope.launch {
            httpClient.webSocket(BINANCE_WEBSOCKET_MINITICKER_URL) {
                Log.d("debug_xxx", "sendSubscribe request = ${gson.toJson(subscribeRequest)}")
                send(gson.toJson(subscribeRequest))
                incoming.receiveAsFlow().collect { frame ->
                    when (frame.frameType) {
                        FrameType.TEXT -> {
                            Log.d("debug_xxx", "TEXT ${(frame as Frame.Text).readText()}")
                        }
                        FrameType.BINARY -> {
                            Log.d("debug_xxx", "BINARY ${frame.readBytes()}")
                        }
                        FrameType.PING -> {
                            Log.d("debug_xxx", "PING ${(frame as Frame.Ping).readBytes()}")
                        }
                        FrameType.PONG -> {
                            Log.d("debug_xxx", "PONG ${(frame as Frame.Pong).readBytes()}")
                        }
                        FrameType.CLOSE -> {
                            Log.d("debug_xxx", "CLOSE ${(frame as Frame.Close).readReason()}")
                        }
                    }
                }
            }
        }
    }

}