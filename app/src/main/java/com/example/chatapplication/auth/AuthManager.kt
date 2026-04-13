package com.example.chatapplication.auth

import androidx.lifecycle.ViewModel
import com.example.chatapplication.prefernces.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class AuthManager @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel(){

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState

    init {
        checkAuth()
    }

    fun checkAuth() {
        val token = sessionManager.getAccessToken()
        _authState.value =
            if (token != null) AuthState.Authenticated
            else AuthState.Unauthenticated
    }

    fun setAuthenticated() {
        _authState.value = AuthState.Authenticated
    }

    fun logout() {
        sessionManager.clearSession()
        _authState.value = AuthState.Unauthenticated
    }
}
sealed class AuthState {
    object  idle: AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated: AuthState()
    data class Error(val message: String) : AuthState()

}

