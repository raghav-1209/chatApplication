package com.example.chatapplication.status

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.models.StatusModel
import com.example.chatapplication.models.StatusWithUser
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.prefernces.SessionManager
import com.example.chatapplication.prefernces.UserPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    val fbAuth: FirebaseAuth,
  val userPreferences: UserPreferences,
    val sessionManager: SessionManager,
    val context: Context,
  val dataBaseRep: DataBaseRep): ViewModel(){

    fun uriToByteArray(uri: Uri):ByteArray{
        val inputStream=context.contentResolver.openInputStream(uri)
        val byteArrayOutPutArray= ByteArrayOutputStream()
        inputStream?.copyTo(byteArrayOutPutArray)
        return byteArrayOutPutArray.toByteArray()
    }

}