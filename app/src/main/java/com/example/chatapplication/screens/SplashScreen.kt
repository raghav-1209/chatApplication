package com.example.chatapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.R
import com.example.chatapplication.navigation.DestinationScreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun splashScreen(onSplash:()->Unit,navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().background(color=Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(Unit) {
            onSplash()
            delay(1500)
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                // User already logged in → go to Welcome screen
                navController.navigate(DestinationScreen.welcomeScreen.route) {
                    popUpTo(DestinationScreen.splashScreen.route) {
                        inclusive = true
                    }


                }
            } else {
                // No user → go to SignIn screen
                navController.navigate(DestinationScreen.signInScreen.route) {
                    popUpTo(DestinationScreen.splashScreen.route) {
                        inclusive = true
                    }
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