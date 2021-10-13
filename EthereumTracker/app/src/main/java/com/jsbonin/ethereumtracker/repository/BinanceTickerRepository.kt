package com.jsbonin.ethereumtracker.repository

import android.util.Log
import com.google.gson.Gson
import com.jsbonin.ethereumtracker.model.BinanceMiniTickerSymbol
import com.jsbonin.ethereumtracker.model.BinanceTickerSubscribeRequest
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BinanceTickerRepository(private val httpClient: HttpClient, private val applicationScope: CoroutineScope) {

    companion object {
        private const val BINANCE_WEBSOCKET_MINITICKER_URL = "wss://stream.binance.com:9443/ws/ethusdt@miniTicker"
        private const val MINI_TICKER_ETHUSD = "ethusdt@miniTicker"
        private const val TICKER_ETHUSD = "ethusd@ticker"
        private const val TAG = "BinanceTickerRepository"
    }

    private val gson = Gson()

    private val subscribeRequest = BinanceTickerSubscribeRequest(
        "SUBSCRIBE",
        params = listOf(MINI_TICKER_ETHUSD),
        id = 1
    )

    private val ethMiniTickerFlow = MutableStateFlow(BinanceMiniTickerSymbol())
    private var webSocketSession: DefaultClientWebSocketSession? = null

    fun ethMiniTickerFlow(): Flow<BinanceMiniTickerSymbol> = ethMiniTickerFlow

    fun connect() {
        applicationScope.launch {
            httpClient.webSocket(BINANCE_WEBSOCKET_MINITICKER_URL) {
                webSocketSession = this
                val subscribeRequestJson = gson.toJson(subscribeRequest)
                Log.d(TAG, "subscribeRequest : $subscribeRequestJson")
                send(subscribeRequestJson)
                incoming.receiveAsFlow().collect { frame ->
                    when (frame.frameType) {
                        FrameType.TEXT -> {
                            val payloadJson = (frame as Frame.Text).readText()
                            Log.d(TAG, "TEXT $payloadJson")
                            ethMiniTickerFlow.emit(gson.fromJson(payloadJson, BinanceMiniTickerSymbol::class.java))
                        }
                        FrameType.PING -> {
                            Log.d(TAG, "PING ${(frame as Frame.Ping).readBytes()}")
                        }
                        FrameType.PONG -> {
                            Log.d(TAG, "PONG ${(frame as Frame.Pong).readBytes()}")
                        }
                        FrameType.CLOSE -> {
                            Log.d(TAG, "CLOSE ${(frame as Frame.Close).readReason()}")
                            webSocketSession = null
                        }
                        FrameType.BINARY -> {
                        }
                    }
                }
            }
        }
    }

    fun close() {
        applicationScope.launch {
            webSocketSession?.close(reason = CloseReason(CloseReason.Codes.GOING_AWAY, "bye bye"))
        }
    }

}