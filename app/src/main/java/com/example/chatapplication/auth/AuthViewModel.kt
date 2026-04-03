package com.example.chatapplication.auth

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.repository.DataBaseRep
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
import android.widget.Toast
import coil.network.HttpException
import com.example.chatapplication.ConnectionController
import com.example.chatapplication.apis.DataBaseApis
import com.example.chatapplication.apis.Info
import com.example.chatapplication.client.WebSocketManager
import com.example.chatapplication.models.FcmData
import com.example.chatapplication.models.SignInData
import com.example.chatapplication.models.loginData
import com.example.chatapplication.prefernces.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await


@HiltViewModel
class AuthViewModel @Inject constructor(val DatabaseRep: DataBaseRep,
    val shrPref: SharedPreferences,
    val apis: DataBaseApis,
    val webSocketManager: WebSocketManager,
    val connectionController: ConnectionController
,
    val fbAuth: FirebaseAuth): ViewModel() {
    private val _authState = MutableStateFlow(false)
    val AuthState = _authState
    private fun currentUid() = fbAuth.currentUser?.uid ?: ""

    fun signIn(email: String, password: String, name: String) {
        val cleanEmail = email.trim()
        val cleanName = email.trim()
        fbAuth.createUserWithEmailAndPassword(cleanEmail, password)
            .addOnSuccessListener {
                Log.d("Auth_VM", "The User ${cleanName} has Signed In")

                val uid = fbAuth.currentUser?.uid ?: return@addOnSuccessListener
                viewModelScope.launch {
                    try {
                        val response=DatabaseRep.saveSignIn(SignInData(
                            cleanEmail,
                            name = name,
                            uid
                        ))
                        shrPref.saveName(uid,name)

                        response.onSuccess {
                            shrPref.saveRefreshToken(uid,it.refreshToken)
                            Log.e("Auth_Vm","the Tokens Are ${it.token}  and refresh Token ${it.refreshToken}")
                            shrPref.saveAccessToken(uid,it.token)
                            connectionController.setLoginState(true, it.token)
                            try {
                                FirebaseMessaging.getInstance().token.addOnSuccessListener {
                                    viewModelScope.launch {
                                        DatabaseRep.saveTokenInDb(
                                            FcmData(
                                                uid,
                                                it
                                            )
                                        )
                                    }
                                }

                            }catch (e: Exception){
                                Log.e("Auth_VM","THE FCM COULDNT SAVED ${e.message}")
                            }
                            Log.e("Auth_Vm","The AccessToken ${it.refreshToken} and The accessToken ${it.token}")
                        }



                    }catch (e: Exception){
                        Log.e("Auth_Vm","The Issue is This Bro ${e.message}")
                    }
                }
                _authState.value=true



            }
            .addOnFailureListener {
                _authState.value=false
                Log.e("Auth_Vm","The SignIn failed ${it.message}")
            }

    }
    init {
        fbAuth.addAuthStateListener { auth ->
            val isLogged = auth.currentUser != null
            _authState.value = isLogged

            if (isLogged) {
                val uid = currentUid()
                val token = shrPref.getAccessToken(uid)
                connectionController.setLoginState(true, token)
            } else {
                connectionController.setLoginState(false)
            }
        }
    }
    fun check(context: Context){
        viewModelScope.launch {
            try {
                val tokenFromPrefs = shrPref.getAccessToken(uid = currentUid()) ?: ""
                val bearerToken = "Bearer $tokenFromPrefs"
                val response = DatabaseRep.check(bearerToken, currentUid())
                response.onSuccess {
                    Toast.makeText(context, " Yo Homie", Toast.LENGTH_LONG).show()
                }
                response.onFailure {
                    Toast.makeText(context, " Who The Fuck Are YOU ", Toast.LENGTH_LONG).show()

                }
            }catch (e: Exception){
                Log.e("Auth_Vm","The Issue From DataBAseRep Was ${e.message}")
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            connectionController.setLoginState(false)
            fbAuth.signOut()
            _authState.value = false
        }
    }

    fun login(email: String, password: String) {
        _authState.value = false
        val cleanEmail = email.trim()
        fbAuth.signInWithEmailAndPassword(cleanEmail, password)
            .addOnFailureListener {
                _authState.value = false
                Log.e("Auth_Vm", "Cannot Login ${it.message}")
            }.addOnSuccessListener {
                val uid = fbAuth.currentUser?.uid ?: return@addOnSuccessListener

                _authState.value = true
                viewModelScope.launch {try {

                    val response = DatabaseRep.loginCheck(
                        loginData(
                            cleanEmail,
                            password
                        )
                    )
                    response.onSuccess { it ->
                        Log.e(
                            "Auth_Vm",
                            "the Tokens Are ${it.token}  and refresh Token ${it.refreshToken}"
                        )
                        shrPref.saveRefreshToken(uid, it.refreshToken)
                        shrPref.saveAccessToken(uid, it.token)
                        connectionController.setLoginState(true, it.token)
                        try {
                            FirebaseMessaging.getInstance().token.addOnSuccessListener {
                                viewModelScope.launch {
                                    DatabaseRep.saveTokenInDb(
                                        FcmData(
                                            uid,
                                            it
                                        )
                                    )
                                }
                            }

                        } catch (e: Exception) {
                            Log.e("Auth_VM", "THE FCM COULDNT SAVED ${e.message}")
                        }


                    }
                    Log.d("Auth_VM", "The $email has been Logged IN")
                    response.onFailure {
                        Log.e("Auth_VM","LoginCheck failed ${it.message}")
                    }

                }catch (e: Exception){
                    Log.e("Auth_Vm","The issue in Login was ${e.message}")
                }

                }
            }
    }

}
