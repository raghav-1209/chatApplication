package com.example.chatapplication.auth

import android.widget.Toast
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.R
import com.example.chatapplication.navigation.DestinationScreen
import com.example.chatapplication.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val authState by authViewModel.AuthState.collectAsState()
    val context= LocalContext.current


    LaunchedEffect(authState) {
        if (authState)
            navController.navigate(DestinationScreen.welcomeScreen.route)
    }

    /* 🔥 ANIMATIONS */
    val infinite = rememberInfiniteTransition(label = "anim")

    val floatY by infinite.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val pulse by infinite.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Login",
                color = Color.White
                , fontSize = 28.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
                    ,
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                )

            ) {

                Box {

                    Box(
                        modifier = Modifier.matchParentSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chat),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.1f),
                            modifier = Modifier
                                .size(150.dp)
                                .graphicsLayer {
                                    translationY = floatY - 80f
                                    scaleX = pulse
                                    scaleY = pulse
                                }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {


                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            colors = darkFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        PasswordField(
                            password
                        ) {
                            password = it
                        }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if(email.isEmpty() || password.isEmpty() )
                                    Toast.makeText(context,"Fill The Credentials",Toast.LENGTH_SHORT).show()
                                else   authViewModel.login(email, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black

                            )

                        ) {
                            Text("Login")
                        }

                        Spacer(Modifier.height(12.dp))

                        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            Text("New here?", color = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Sign Up",
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        DestinationScreen.signInScreen.route
                                    )
                                },
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }
}
