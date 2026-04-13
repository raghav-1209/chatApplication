package com.example.chatapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.R
import com.example.chatapplication.auth.AuthManager
import com.example.chatapplication.auth.AuthState
import com.example.chatapplication.navigation.DestinationScreen

@Composable
fun splashScreen(onSplash:()->Unit,navController: NavController,authManager: AuthManager){
    Column(
        modifier = Modifier.fillMaxSize().background(color=Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val state = authManager.authState.collectAsState()
        val hasNavigated = remember { mutableStateOf(false) }   //  guard

        LaunchedEffect(state.value) {
            if(hasNavigated.value)
                return@LaunchedEffect

                when (state.value) {

                    is AuthState.Authenticated -> {
                        onSplash()
                        navController.navigate(DestinationScreen.welcomeScreen.route) {
                            popUpTo(DestinationScreen.splashScreen.route) {
                                inclusive = true
                            }
                        }
                    }

                    is AuthState.Unauthenticated -> {
                        onSplash()
                        navController.navigate(DestinationScreen.signInScreen.route) {
                            popUpTo(DestinationScreen.splashScreen.route) {
                                inclusive = true
                            }
                        }
                    }

                    else -> {


                    }
                }
            }




        Image(
            painter = painterResource(R.drawable.chat),
            contentDescription = null,
            modifier = Modifier.size(90. dp),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}