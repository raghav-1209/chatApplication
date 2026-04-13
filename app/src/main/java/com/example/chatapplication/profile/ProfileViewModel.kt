package com.example.chatapplication.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.FollowState
import com.example.chatapplication.auth.LoginScreen
import com.example.chatapplication.models.BioData
import com.example.chatapplication.models.UserData
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.models.isOnline
import com.example.chatapplication.prefernces.SessionManager
import com.example.chatapplication.prefernces.UserPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val dataBaseRep: DataBaseRep,
    val fbAuth: FirebaseAuth,
    val userPreferences: UserPreferences,
    val sessionManager: SessionManager,
    val context: Context
) : ViewModel(){




}
