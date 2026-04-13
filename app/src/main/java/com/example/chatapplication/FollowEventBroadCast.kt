package com.example.chatapplication

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.chatapplication.repository.DataBaseRep
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class FollowEventBroadCast : BroadcastReceiver() {
    @Inject
    lateinit var dataBaseRep: DataBaseRep
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return

        val senderId = intent?.getIntExtra("senderId", -1)?:return
        val receiverUid=intent.getStringExtra("receiverUid")?:""
        val senderUid=intent.getStringExtra("senderUid")?:""
        val notificationId = intent.getIntExtra("notificationId", -1)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.cancel(notificationId)
        if(senderId==-1)
            return
        CoroutineScope(Dispatchers.IO).launch {
            when (intent.action) {

                "ACTION_ACCEPT" -> {
                    Log.e("FollowListenBroadCast","The Accept action Called")
//                    dataBaseRep.acceptFollow(receiverUid,senderUid)




                }

                "ACTION_REJECT" -> {
                    Log.e("FollowListenBroadCast","The Accept action Called")
//                        dataBaseRep.rejectFollow(receiverUid, senderUid)

                }

            }
        }
    }
}