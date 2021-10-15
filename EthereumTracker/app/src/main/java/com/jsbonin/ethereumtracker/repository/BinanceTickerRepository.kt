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
        private const val BINANCE_WEBSOCKET_MINITICKER_ETHUSDT_URL = "wss://stream.binance.com:9443/ws/ethusdt@miniTicker"
        private const val BINANCE_WEBSOCKET_MINITICKER_ETHBTC_URL = "wss://stream.binance.com:9443/ws/ethbtc@miniTicker"
        private const val MINI_TICKER_ETHUSD = "ethusdt@miniTicker"
        private const val MINI_TICKER_ETHBTC = "ethbtc@miniTicker"
        private const val TAG = "BinanceTickerRepository"
    }

    private val gson = Gson()

    private val subscribeRequestETHUSDT = BinanceTickerSubscribeRequest(
        "SUBSCRIBE",
        params = listOf(MINI_TICKER_ETHUSD),
        id = 1
    )
    private val subscribeRequestETHBTC = BinanceTickerSubscribeRequest(
        "SUBSCRIBE",
        params = listOf(MINI_TICKER_ETHBTC),
        id = 1
    )

    private val miniTickerETHUSDTFlow = MutableStateFlow(BinanceMiniTickerSymbol())
    private val miniTickerETHBTCFlow = MutableStateFlow(BinanceMiniTickerSymbol())
    private var webSocketSessionETHUSDT: DefaultClientWebSocketSession? = null
    private var webSocketSessionETHBTC: DefaultClientWebSocketSession? = null

    fun miniTickerETHUSDTFlow(): Flow<BinanceMiniTickerSymbol> = miniTickerETHUSDTFlow

    fun miniTickerETHBTCFlow(): Flow<BinanceMiniTickerSymbol> = miniTickerETHBTCFlow

    fun connect() {
        applicationScope.launch {
            httpClient.webSocket(BINANCE_WEBSOCKET_MINITICKER_ETHUSDT_URL) {
                webSocketSessionETHUSDT = this
                val subscribeRequestJson = gson.toJson(subscribeRequestETHUSDT)
                Log.d(TAG, "subscribeRequestETHUSDT : $subscribeRequestJson")
                send(subscribeRequestJson)
                incoming.receiveAsFlow().collect { frame ->
                    when (frame.frameType) {
                        FrameType.TEXT -> {
                            val payloadJson = (frame as Frame.Text).readText()
                            Log.d(TAG, "TEXT $payloadJson")
                            miniTickerETHUSDTFlow.emit(gson.fromJson(payloadJson, BinanceMiniTickerSymbol::class.java))
                        }
                        FrameType.PING -> {
                        }
                        FrameType.PONG -> {
                        }
                        FrameType.CLOSE -> {
                            Log.d(TAG, "CLOSE ${(frame as Frame.Close).readReason()}")
                            webSocketSessionETHUSDT = null
                        }
                        FrameType.BINARY -> {
                        }
                    }
                }
            }
        }
        applicationScope.launch {
            httpClient.webSocket(BINANCE_WEBSOCKET_MINITICKER_ETHBTC_URL) {
                webSocketSessionETHBTC = this
                val subscribeRequestJson = gson.toJson(subscribeRequestETHBTC)
                Log.d(TAG, "subscribeRequestETHBTC : $subscribeRequestJson")
                send(subscribeRequestJson)
                incoming.receiveAsFlow().collect { frame ->
                    when (frame.frameType) {
                        FrameType.TEXT -> {
                            val payloadJson = (frame as Frame.Text).readText()
                            Log.d(TAG, "TEXT $payloadJson")
                            miniTickerETHBTCFlow.emit(gson.fromJson(payloadJson, BinanceMiniTickerSymbol::class.java))
                        }
                        FrameType.PING -> {
                        }
                        FrameType.PONG -> {
                        }
                        FrameType.CLOSE -> {
                            Log.d(TAG, "CLOSE ${(frame as Frame.Close).readReason()}")
                            webSocketSessionETHBTC = null
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
            webSocketSessionETHUSDT?.close(reason = CloseReason(CloseReason.Codes.GOING_AWAY, "bye bye"))
            webSocketSessionETHBTC?.close(reason = CloseReason(CloseReason.Codes.GOING_AWAY, "bye bye"))
        }
    }

}