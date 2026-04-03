package com.example.chatapplication.client

import android.content.SharedPreferences
import android.util.Log
import coil.network.HttpException
import com.example.chatapplication.apis.DataBaseApis
import com.example.chatapplication.apis.Info
import com.example.chatapplication.models.ChatData
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.headers
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class  WebSocketManager @Inject constructor(
    val client: HttpClient,
    val apis: DataBaseApis,
    val fbAuth: FirebaseAuth,
    val sharedPreferences: com.example.chatapplication.prefernces.SharedPreferences
) {

    private var session: DefaultClientWebSocketSession? = null
    private val socketScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _incomingMessages = MutableSharedFlow<String>()
    val incomingMessages = _incomingMessages.asSharedFlow()
    val uid=fbAuth.currentUser?.uid?:""

    suspend fun connect(accessToken: String) {

        if (session != null) return

        try {
            session = client.webSocketSession {
                url("ws://10.0.2.2:8080/connect")
                header("Authorization", "Bearer $accessToken")
            }

            startListening()

        } catch (e: ClientRequestException) {

            if (e.response.status.value == 401) {
                Log.e("WebSocket", "Access token expired")
                refreshAndReconnect()
            }

        } catch (e: Exception) {
            Log.e("WebSocket", "Connection error: ${e.message}")
        }
    }
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun sendMessage(chatData: ChatData) {
        val jsonString = json.encodeToString(chatData)
        session?.send(jsonString)
    }

    private fun startListening() {
        socketScope.launch {
            session?.let { session ->
                try {
                    for (frame in session.incoming) {
                        val message = (frame as? Frame.Text)?.readText()
                        message?.let {
                            _incomingMessages.emit(it)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Listen error ${e.message}")
                }
            }
        }
    }

    suspend fun disconnect() {
        session?.close()
        session = null
    }
    private suspend fun refreshAndReconnect() {

        try {
            val refreshToken = sharedPreferences.getRefreshToken(uid) ?: return

            val response = apis.refreshToken(Info(refreshToken))

            sharedPreferences.saveAccessToken(uid, response.token)
            sharedPreferences.saveRefreshToken(uid, response.refreshToken)

            connect(response.token)

        } catch (e: Exception) {
            Log.e("WebSocket", "Refresh failed ${e.message}")
            disconnect()
        }
    }
}
