package com.example.chatapplication.auth

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.R
import com.example.chatapplication.navigation.DestinationScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(authViewModel: AuthViewModel,navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.collectAsState()
    val context=LocalContext.current


    LaunchedEffect(authState) {
        when (authState) {

            is AuthState.Authenticated -> {
                navController.navigate(DestinationScreen.welcomeScreen.route) {
                    popUpTo(DestinationScreen.signInScreen.route) {
                        inclusive = true
                    }
                }
            }

            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

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
                text = "SignIn",
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
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("name") },
                            singleLine = true,
                            colors = darkFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

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
                                if(email.isEmpty() || password.isEmpty() || name.isEmpty())
                                    Toast.makeText(context,"Fill The Credentials",Toast.LENGTH_SHORT).show()
                                else
                                    authViewModel.signIn(email,password,name)
                            },
                            enabled = authState !is AuthState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        ) {
                            if (authState is AuthState.Loading) {
                                CircularProgressIndicator()
                            } else {
                                Text("SignIn")
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                            Text("Already here?", color = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Login",
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        DestinationScreen.loginScreen.route
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

/* ---------------- TEXTFIELD COLORS ---------------- */
@Composable
 fun darkFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
    cursorColor = MaterialTheme.colorScheme.primary
)
@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        colors = darkFieldColors(),

        label = { Text("Password") },
        singleLine = true,

        visualTransformation =
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),

        trailingIcon = {
            val icon =
                if (passwordVisible) Icons.Default.Visibility
                else Icons.Default.VisibilityOff

            IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Toggle Password Visibility"
                )
            }
        }
    )
}





