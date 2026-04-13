package com.example.chatapplication.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.ConnectionController
import com.example.chatapplication.client.WebSocketManager
import com.example.chatapplication.models.FcmData
import com.example.chatapplication.models.SignInData
import com.example.chatapplication.models.loginData
import com.example.chatapplication.prefernces.UserPreferences
import com.example.chatapplication.repository.DataBaseRep
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val databaseRep: DataBaseRep,
    private val userPreferences: UserPreferences,
    private val webSocketManager: WebSocketManager,
    private val connectionController: ConnectionController,
    private val fbAuth: FirebaseAuth,
    private val authManager: AuthManager
) : ViewModel() {

    private fun currentUid() = fbAuth.currentUser?.uid ?: ""
    private val _authState = MutableStateFlow<AuthState>(AuthState.idle)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(email: String, password: String, name: String) {
        _authState.value = AuthState.Loading
        val cleanEmail = email.trim()
        val cleanName = name.trim()

        fbAuth.createUserWithEmailAndPassword(cleanEmail, password)
            .addOnSuccessListener {


                val uid = currentUid()

                viewModelScope.launch {
                    try {
                        val response = databaseRep.signIn(
                            SignInData(cleanEmail, name = cleanName, uid)
                        )

                        userPreferences.saveName(cleanName)

                        response.onSuccess {
                            Log.d("Auth_VM", "SignIn success")
                            _authState.value = AuthState.Authenticated

                            //  Setup system first
                            connectionController.setLoginState(true, it.token)

                            //  THEN update UI state
                            authManager.setAuthenticated()

                            //  Save FCM
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { fcm ->
                                viewModelScope.launch {
                                    databaseRep.saveTokenInDb(FcmData(uid, fcm))
                                }
                            }
                        }

                        response.onFailure {
                            Log.e("Auth_VM", "Backend SignIn failed ${it.message}")
                            _authState.value = AuthState.Error(it.message?:"something went wrong")

                            authManager.logout()
                        }

                    } catch (e: Exception) {
                        Log.e("Auth_VM", "SignIn exception ${e.message}")
                        authManager.logout()
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Auth_VM", "Firebase SignIn failed ${it.message}")
            }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        val cleanEmail = email.trim()

        fbAuth.signInWithEmailAndPassword(cleanEmail, password)
            .addOnFailureListener {
                Log.e("Auth_VM", "Firebase Login failed ${it.message}")
            }
            .addOnSuccessListener {

                val uid = currentUid()

                viewModelScope.launch {
                    try {
                        val response = databaseRep.login(
                            loginData(cleanEmail, password),
                            uid
                        )

                        response.onSuccess {
                            Log.d("Auth_VM", "Login success")
                            _authState.value = AuthState.Authenticated

                            // Setup system first
                            connectionController.setLoginState(true, it.token)

                            //  THEN update UI
                            authManager.setAuthenticated()

                            //  Save FCM
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { fcm ->
                                viewModelScope.launch {
                                    databaseRep.saveTokenInDb(FcmData(uid, fcm))
                                }
                            }
                        }

                        response.onFailure {
                            Log.e("Auth_VM", "Backend login failed ${it.message}")
                            _authState.value = AuthState.Error(it.message?:"something went wrong")

                            authManager.logout()
                        }

                    } catch (e: Exception) {
                        Log.e("Auth_VM", "Login exception ${e.message}")
                        authManager.logout()
                    }
                }
            }
    }


    fun logout() {
        viewModelScope.launch {
            fbAuth.signOut()
            connectionController.setLoginState(false)
            authManager.logout()
        }
    }

    fun check(context: Context) {
        viewModelScope.launch {
            try {
                val response = databaseRep.check()

                response.onSuccess {
                    Toast.makeText(context, "Yo Homie", Toast.LENGTH_LONG).show()
                }

                response.onFailure {
                    Toast.makeText(context, "Who are you?", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Log.e("Auth_VM", "Check error ${e.message}")
            }
        }
    }

    fun getName(): String {
        return userPreferences.getName() ?: ""
    }
}