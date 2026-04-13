package com.example.chatapplication

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.chatapplication.client.WebSocketManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppLifecycleObserver @Inject constructor(
    private val connectionController: ConnectionController
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        connectionController.setAppForeground(true)
    }

    override fun onStop(owner: LifecycleOwner) {
        connectionController.setAppForeground(false)
    }
}