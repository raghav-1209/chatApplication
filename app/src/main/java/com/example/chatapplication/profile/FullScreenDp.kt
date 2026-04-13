package com.example.chatapplication.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.chatapplication.chats.ChatViewModel

@Composable
fun FullScreenDp(uid: String,chatViewModel: ChatViewModel){
//    val user_Info=chatViewModel.User_Info.collectAsState()
//    val image=user_Info.value?.image?:""
//    LaunchedEffect(uid) {
//        chatViewModel.fetchUser(uid)
//    }
//    Box(
//        modifier = Modifier.fillMaxSize().background(Color.Black),
//        contentAlignment = Alignment.Center
//    ) {
//        if(image.isEmpty()){
//            Text("No Profile", textAlign = TextAlign.Center)
//        }else {
//
//            AsyncImage(
//                model = image,
//                contentDescription = "Profile Picture",
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//
//    }

}