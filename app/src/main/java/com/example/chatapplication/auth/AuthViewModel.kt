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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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


    fun signIn(email: String, password: String, name: String) {
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

                            //  Setup system first
                            connectionController.setLoginState(true, it.token)

                            //  THEN update UI state
                            authManager.setAuthenticated()

                            //  Save FCM
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { fcm ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        databaseRep.saveTokenInDb(FcmData(uid, fcm))
                                    } catch (e: Exception) {
                                        Log.e("FCM", "Failed: ${e.message}")
                                    }
                                }
                            }
                        }

                        response.onFailure {
                            Log.e("Auth_VM", "Backend SignIn failed ${it.message}")

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

                            // Setup system first
                            connectionController.setLoginState(true, it.token)

                            //  THEN update UI
                            authManager.setAuthenticated()

                            //  Save FCM
                            FirebaseMessaging.getInstance().token.addOnSuccessListener { fcm ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        databaseRep.saveTokenInDb(FcmData(uid, fcm))
                                    } catch (e: Exception) {
                                        Log.e("FCM", "Failed: ${e.message}")
                                    }
                                }
                            }
                        }

                        response.onFailure {
                            Log.e("Auth_VM", "Backend login failed ${it.message}")

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

    private val _checkState = MutableStateFlow<Boolean?>(null)
    val checkState = _checkState

    fun check(context:Context) {
        viewModelScope.launch {
            val response = databaseRep.check()
            response.onFailure {
                Toast.makeText(context,"Who Are U",Toast.LENGTH_LONG).show()

                Log.e("Auth_Vm","${it.message}")
            }
            response.onSuccess {
                Toast.makeText(context,"Yo Homie",Toast.LENGTH_LONG).show()

                _checkState.value = it.success
            }
        }
    }

    fun getName(): String {
        return userPreferences.getName() ?: ""
    }
}