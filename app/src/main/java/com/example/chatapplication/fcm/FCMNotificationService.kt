package com.example.chatapplication.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.chatapplication.FollowEventBroadCast
import com.example.chatapplication.R
import com.example.chatapplication.models.FcmData
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@AndroidEntryPoint
class FCMNotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var dataBaseRep: DataBaseRep

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val serviceScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    lateinit var fbAuth: FirebaseAuth
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val uid = firebaseAuth.currentUser?.uid
        if (uid == null) {
            Log.w("FCM", "User not logged in, skipping token save")
            return
        }
        val fcmData = FcmData(token = token, uid = uid)

      serviceScope.launch {
            try {
                dataBaseRep.saveTokenInDb(fcmData)
                Log.d("FCM", "Token saved successfully")
            } catch (e: Exception) {
                Log.e("FCM", "Token save failed", e)
            }
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("FCM","${message}")
        Log.e("FCM_DATA", message.data.toString())
        Log.e("FCM_NOTI", message.notification?.body.toString())

       if(message.data["type"]=="follow_request") {
           val senderId =
               message.data["senderId"]?.toInt()?:0

           val receiverId =
               message.data["receiverId"]?.toInt()?:0
           val title=message.data["title"]?:"followRequest"
           val body=message.data["body"]?:"New follower"
           val senderUid=message.data["senderUid"]?:""
           val receiverUid=message.data["receiverUid"]?:""


           showNotification(title = title,body = body,senderId, receiverId = receiverId, receiverUid = receiverUid,senderUid)

       }else if(message.data["type"]=="chat"){
           val senderUid=message.data["senderUid"]?:""
           val receiverUid=message.data["receiverUid"]?:""
               val text=message.data["text"]?:"New Message"
           val senderName=message.data["senderName"]?:"Unknown"


           MessageNotification(senderUid,receiverUid,text, name = senderName)

       }



    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
    fun showNotification(
        title: String,
        body: String,
        senderId: Int,
        receiverId: Int,
        receiverUid: String,
        senderUid: String
    ) {
        val notificationId = System.currentTimeMillis().toInt()


        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "chat_channel",
                "Chat Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        val acceptIntent = Intent(this, FollowEventBroadCast::class.java).apply {
            action = "ACTION_ACCEPT"
            putExtra("senderId", senderId)

            putExtra("receiverId", receiverId)
            putExtra("receiverUid", receiverUid)
            putExtra("senderUid", senderUid)
            putExtra("notificationId", notificationId)
        }

        val rejectIntent = Intent(this, FollowEventBroadCast::class.java).apply {
            action = "ACTION_REJECT"
            putExtra("senderId", senderId)
            putExtra("receiverId", receiverId)
            putExtra("notificationId", notificationId)
        }


        val acceptPendingIntent = PendingIntent.getBroadcast(
            this,
            senderId * 10 + 1,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val rejectPendingIntent = PendingIntent.getBroadcast(
            this,
            senderId * 10 + 2,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "chat_channel")
            .setSmallIcon(R.drawable.chat)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(0, "Accept", acceptPendingIntent)
            .addAction(0, "Reject", rejectPendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)


        manager.notify(notificationId, builder.build())
    }
    fun MessageNotification(senderUid: String,receiverUid: String,text: String,name: String){
        val notificationId = System.currentTimeMillis().toInt()


        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "message_channel",
                "ChatMessage_Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(this, "message_channel")
            .setSmallIcon(R.drawable.chat)
            .setContentTitle(name)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)


        manager.notify(notificationId, builder.build())

    }






}
