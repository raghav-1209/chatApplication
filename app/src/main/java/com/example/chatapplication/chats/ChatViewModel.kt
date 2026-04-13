package com.example.chatapplication.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.client.WebSocketManager
import com.example.chatapplication.models.ChatData
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.prefernces.SessionManager
import com.example.chatapplication.prefernces.UserPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val dataBaseRep: DataBaseRep,
    val fbAuth: FirebaseAuth,
    val sessionManager: SessionManager,
    val userPreferences: UserPreferences,
    val webSocketManager: WebSocketManager
) : ViewModel() {

//    fun currUid(): String{
//        return  fbAuth.currentUser?.uid?:""
//    }
//    private val userInfo= MutableStateFlow<WholeUser?>(null)
//    val User_Info=userInfo.asStateFlow()
//    fun fetchUser(uid: String){
//        viewModelScope.launch {
//            val currUid=currUid()
//            val response=dataBaseRep.getUserByUid(uid)
//            response.onSuccess {
//                Log.e("Chat_Vm","the USer Info ${it}")
//                userInfo.value=it
//            }
//            response.onFailure {
//                Log.e("Chat_Vm","the Issue To Get Info From DBRep ${it.message}")
//            }
//        }
//    }
//    fun generateChatId(u1: String, u2: String): String {
//        return listOf(u1, u2).sorted().joinToString("+")
//    }
//    private val _messages = MutableStateFlow<List<ChatData>>(emptyList())
//    val Messages = _messages.asStateFlow()
//    fun sendMessage(message: String, receiverUid: String) {
//        val message_id= UUID.randomUUID().toString()
//        val chat = ChatData(
//            messageId = message_id,
//            chat_id = generateChatId(receiverUid,currUid()),
//            senderUid = currUid(),
//            receiverUid = receiverUid,
//            message = message,
//            time= System.currentTimeMillis()
//        )
//        viewModelScope.launch {
//            try {
//                webSocketManager.sendMessage(chat)
//                _messages.value = (_messages.value + chat)
//                    .sortedBy { it.time }
//
//            } catch (e: Exception) {
//                Log.e("Chat_Vm", "cannot Message ${e.message}")
//            }
//        }
//    }
//    private val json = Json {
//        ignoreUnknownKeys = true
//    }
//
//
//    init {
//        viewModelScope.launch {
//
//            webSocketManager.incomingMessages.collect { jsonString ->
//
//                val chatData = withContext(Dispatchers.Default) {
//                    json.decodeFromString<ChatData>(jsonString)
//                }
//
//                if (_messages.value.none { it.messageId == chatData.messageId }) {
//                    _messages.value += chatData
//                }
//            }
//        }
//    }
//
//
//    private var currentChatId: String? = null
//    fun getMessage(receiver_Uid: String) {
//        val chat_id = generateChatId(currUid(), receiver_Uid)
//        currentChatId = chat_id
//
//        viewModelScope.launch {
//
//            val response = dataBaseRep.getMessages(chat_id, )
//
//            response.onSuccess {
//                _messages.value = it.sortedBy { msg -> msg.time }
//            }
//        }
//    }
//    private val _aiBotChat = MutableStateFlow<List<ChatMessage>>(emptyList())
//    val aiBotChat = _aiBotChat.asStateFlow()
//
//    fun sendMessageToBot(message: String) {
//        viewModelScope.launch {
//            _aiBotChat.value += ChatMessage(message, true)
//            val uid = currUid()
//
//            val response = withContext(Dispatchers.IO) {
//                dataBaseRep.sendMessageToAi(uid,)
//            }
//            response.onSuccess { reply ->
//                _aiBotChat.value += ChatMessage(reply.message, false)
//
//            }.onFailure {
//                _aiBotChat.value += ChatMessage("Error occurred", false)
//            }
//        }
//    }
//    fun clearChat(receiverUid: String){
//        val chat_id=generateChatId(receiverUid,currUid())
//        viewModelScope.launch {
//            dataBaseRep.deleteChat(chat_id)
//            getMessage(receiverUid)
//        }
//    }
//    fun deleteMessage(message_id: String){
//        viewModelScope.launch {
//            dataBaseRep.deleteMessage(message_id)
//            _messages.value = _messages.value.filter {
//                it.messageId != message_id
//            }
//        }
//    }
//

}
data class ChatMessage(
    val message: String,
    val isUser: Boolean
)
