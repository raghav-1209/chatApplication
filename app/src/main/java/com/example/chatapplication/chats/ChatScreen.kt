package com.example.chatapplication.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapplication.navigation.DestinationScreen
import com.example.chatapplication.profile.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val BackgroundBlack = Color(0xFF000000)
val BubbleMe = Color(0xFF1F8EFA)
private val BubbleOther = Color(0xFF2A2A2A)
private val AccentBlue = Color(0xFF64B5F6)
val InputBackground = Color(0xFF1E1E1E)

@Composable
fun ChatScreen(
    uid: String,
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    val userInfo by chatViewModel.User_Info.collectAsState()
    val messages by chatViewModel.Messages.collectAsState()
    val status by profileViewModel.isOnline.collectAsState()

    val name = userInfo?.credentials?.name ?: ""
    val image = userInfo?.image
    val statusText = status?.text ?: ""

    LaunchedEffect(uid) {
        chatViewModel.fetchUser(uid)
        profileViewModel.checkStatus(uid)
        chatViewModel.getMessage(uid)
    }

    Scaffold(
        containerColor = BackgroundBlack,
        topBar = {
            ChatTopBar(name, image, statusText, navController =navController, uid = uid ){
                chatViewModel.clearChat(uid)
            }
        },
        bottomBar = {
            ChatInputBar {
                chatViewModel.sendMessage(it, uid)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundBlack)
                .padding(padding)
                .padding(horizontal = 12.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages.reversed()) { message ->
                ChatBubble(
                    message = message.message,
                    isMe = message.senderUid == chatViewModel.currUid(),
                    time = message.time
                ){
                    chatViewModel.deleteMessage(message.messageId)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    name: String,
    image: String?,
    status: String,
    navController: NavController,
    uid: String,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundBlack
        ),

        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },

        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.navigate(
                        DestinationScreen.userProfileScreen.createRoute(uid)
                    )
                }
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(name, color = Color.White, fontSize = 16.sp)
                    Text(status, color = AccentBlue, fontSize = 12.sp)
                }
            }
        },

        actions = {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete Chat") },
                        onClick = {
                            menuExpanded = false
                            onDelete()
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ChatBubble(message: String, isMe: Boolean, time: Long,onDelete:()->Unit
) {

    val uriHandler = LocalUriHandler.current
    val formattedTime = remember(time) { formatTime(time) }

    val annotatedString = remember(message) {
        buildAnnotatedString {
            append(message)
            val urlRegex = """https?://[^\s]+""".toRegex()

            urlRegex.findAll(message).forEach {
                addStyle(
                    SpanStyle(
                        color = AccentBlue,
                        textDecoration = TextDecoration.Underline
                    ),
                    it.range.first,
                    it.range.last + 1
                )
                addStringAnnotation(
                    "URL",
                    it.value,
                    it.range.first,
                    it.range.last + 1
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {
                onDelete()
            })
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isMe) BubbleMe else BubbleOther,
                    RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {

            Column {

                ClickableText(
                    text = annotatedString,
                    style = TextStyle(color = Color.White),
                    onClick = { offset ->
                        annotatedString
                            .getStringAnnotations("URL", offset, offset)
                            .firstOrNull()
                            ?.let { uriHandler.openUri(it.item) }
                    }
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = formattedTime,
                    color = Color.Black,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    onSendClick: (String) -> Unit
) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
            .background(BackgroundBlack)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Message", color = Color.Gray)
            },
            textStyle = TextStyle(color = Color.White),
            maxLines = 4,
            singleLine = true,

            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (message.isNotBlank()) {
                        onSendClick(message.trim())
                        message = ""
                    }
                }
            ),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBackground,
                unfocusedContainerColor = InputBackground,
                cursorColor = BubbleMe,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (message.isNotBlank()) {
                    onSendClick(message.trim())
                    message = ""
                }
            },
            modifier = Modifier
                .size(48.dp)
                .background(BubbleMe, CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}