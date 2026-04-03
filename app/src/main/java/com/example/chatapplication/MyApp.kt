package com.example.chatapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import okhttp3.WebSocket
import javax.inject.Inject

@HiltAndroidApp
class MyApp: Application(){
    @Inject lateinit var observer: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(observer)
    }
}
