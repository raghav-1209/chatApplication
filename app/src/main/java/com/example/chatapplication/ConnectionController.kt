package com.example.chatapplication

import com.example.chatapplication.client.WebSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionController @Inject constructor(
    private val webSocketManager: WebSocketManager
) {

    private var isLoggedIn = false
    private var isAppForeground = false
    private var token: String? = null
    val serviceScope= CoroutineScope(SupervisorJob()+ Dispatchers.IO)

    fun setLoginState(loggedIn: Boolean, accessToken: String? = null) {
        isLoggedIn = loggedIn
        token = accessToken
        evaluateConnection()
    }

    fun setAppForeground(foreground: Boolean) {
        isAppForeground = foreground
        evaluateConnection()
    }

    private fun evaluateConnection() {
        if (isLoggedIn && isAppForeground && token != null) {
            serviceScope.launch {
                webSocketManager.connect(token!!)
            }
        } else {
            serviceScope.launch {
                webSocketManager.disconnect()
            }
        }
    }
}