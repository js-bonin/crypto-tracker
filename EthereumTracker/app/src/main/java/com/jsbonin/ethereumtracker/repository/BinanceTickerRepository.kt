package com.jsbonin.ethereumtracker.repository

import android.util.Log
import androidx.compose.runtime.collectAsState
import com.google.gson.Gson
import com.jsbonin.ethereumtracker.EthereumTrackerApplication
import com.jsbonin.ethereumtracker.model.BinanceTickerSubscribeRequest
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BinanceTickerRepository(private val httpClient: HttpClient, private val applicationScope: CoroutineScope) {

    companion object {
        const val BINANCE_WEBSOCKET_MINItICKER_URL = "wss://stream.binance.com:9443/ws/ethusdt@miniTicker"
        const val MINI_TICKER_ETHUSD = "ethusdt@miniTicker"
        const val TICKER_ETHUSD = "ethusd@ticker"
    }

    private val gson = Gson()

    private val subscribeRequest = BinanceTickerSubscribeRequest(
        "SUBSCRIBE",
        params = listOf(MINI_TICKER_ETHUSD),
        id = 1
    )

    fun connect(){
        applicationScope.launch {
            httpClient.webSocket(BINANCE_WEBSOCKET_MINItICKER_URL) {
                Log.d("debug_xxx", "sendSubscribe request = ${gson.toJson(subscribeRequest)}")
                send(gson.toJson(subscribeRequest))
                incoming.receiveAsFlow().collect {  frame ->
                    Log.d("debug_xxx","frame = ${frame.frameType.name}")
//                    while (true){
                        when(frame.frameType){
                            FrameType.TEXT -> {
                                Log.d("debug_xxx","TEXT ${(frame as Frame.Text).readText()}")
                            }
                            FrameType.BINARY -> {
                                Log.d("debug_xxx","BINARY ${frame.readBytes()}")
                            }
                            FrameType.PING -> {
                                Log.d("debug_xxx","PING ${(frame as Frame.Ping).readBytes()}")
                            }
                            FrameType.PONG -> {
                                Log.d("debug_xxx","PONG ${(frame as Frame.Pong).readBytes()}")
                            }
                            FrameType.CLOSE -> {
                                Log.d("debug_xxx","CLOSE ${(frame as Frame.Close).readReason()}")
                            }
                        }
//                    }
                }
//                while (true){
//                    outputMessages()
//                }
//                launch { sendSubscribe() }
//                launch { outputMessages() }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            Log.d("debug_xxx", "outputMessages start")
            Log.d("debug_xxx", "outputMessages isEmpty ${incoming.isEmpty}")
            Log.d("debug_xxx", "outputMessages onReceiveCatching ${incoming.onReceiveCatching}")
            for (message in incoming) {
                message as? Frame.Text ?: continue
                println(message.readText())
                Log.d("debug_xxx", "receivedMessage inside loop${message.readText()}")
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.localizedMessage)
            Log.d("debug_xxx", "Error while receiving ${e.localizedMessage}")
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendSubscribe() {
        try {
            Log.d("debug_xxx", "sendSubscribe request = ${gson.toJson(subscribeRequest)}")
            send(gson.toJson(subscribeRequest))
        } catch (e: Exception) {
            println("Error while sending: " + e.localizedMessage)
            return
        }
    }

}