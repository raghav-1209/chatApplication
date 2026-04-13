package com.example.chatapplication.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.R

@Composable
fun ChatBotScreen(
    chatViewModel: ChatViewModel,
    navController: NavController
) {

//    val messages by chatViewModel.aiBotChat.collectAsState()
//    var message by remember { mutableStateOf("") }
//
//    val listState = rememberLazyListState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(BackgroundBlack)
//            .padding(20.dp)
//    ) {
//
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Black)
//                .padding(horizontal = 8.dp, vertical = 12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            IconButton(onClick = { navController.popBackStack() }) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = null,
//                    tint = Color.White,
//                    modifier = Modifier.size(30.dp)
//                )
//            }
//
//            Spacer(Modifier.width(6.dp))
//
//            Icon(
//                painter = painterResource(R.drawable.bot),
//                contentDescription = null,
//                tint = Color.White,
//                modifier = Modifier.size(36.dp)
//            )
//
//            Spacer(Modifier.width(10.dp))
//
//            Column {
//
//                Text(
//                    text = "AI Assistant",
//                    color = Color.White
//                )
//
//                Text(
//                    text = "Online",
//                    color = Color.Gray
//                )
//            }
//        }
//
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 10.dp),
//            state = listState,
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            contentPadding = PaddingValues(vertical = 16.dp)
//        ) {
//
//            items(messages) { chat ->
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement =
//                        if (chat.isUser) Arrangement.End else Arrangement.Start
//                ) {
//
//                    if (!chat.isUser) {
//
//                        Icon(
//                            painter = painterResource(R.drawable.bot),
//                            contentDescription = null,
//                            tint = Color.White,
//                            modifier = Modifier
//                                .size(32.dp)
//                                .padding(end = 6.dp)
//                        )
//                    }
//
//                    Column {
//
//                        Text(
//                            text = chat.message,
//                            modifier = Modifier
//                                .background(
//                                    if (chat.isUser) BubbleMe else InputBackground,
//                                    shape = RoundedCornerShape(
//                                        topStart = 16.dp,
//                                        topEnd = 16.dp,
//                                        bottomStart =
//                                            if (chat.isUser) 16.dp else 4.dp,
//                                        bottomEnd =
//                                            if (chat.isUser) 4.dp else 16.dp
//                                    )
//                                )
//                                .padding(
//                                    horizontal = 14.dp,
//                                    vertical = 10.dp
//                                )
//                                .widthIn(max = 260.dp),
//                            color = Color.White
//                        )
//                    }
//                }
//            }
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(20.dp)
//                .background(InputBackground, RoundedCornerShape(30.dp))
//
//
//        ,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            TextField(
//                value = message,
//                onValueChange = { message = it },
//                modifier = Modifier.weight(1f),
//                placeholder = {
//                    Text("Ask AI anything...", color = Color.Gray)
//                },
//                maxLines = 4,
//                singleLine = true,
//
//                keyboardOptions = KeyboardOptions.Default.copy(
//                    imeAction = ImeAction.Send
//                ),
//                keyboardActions = KeyboardActions(
//                    onSend = {
//                        if (message.isNotBlank()) {
//                            chatViewModel.sendMessageToBot(message)
//                            message = ""
//                        }
//                    }
//                ),
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color.Transparent,
//                    unfocusedContainerColor = Color.Transparent,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    cursorColor = BubbleMe,
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White
//                )
//            )
//
//            IconButton(
//                onClick = {
//
//                    if (message.isNotBlank()) {
//
//                        chatViewModel.sendMessageToBot(message)
//                        message = ""
//                    }
//                }
//            ) {
//
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.Send,
//                    contentDescription = null,
//                    tint = BubbleMe
//                )
//            }
//        }
//    }
}