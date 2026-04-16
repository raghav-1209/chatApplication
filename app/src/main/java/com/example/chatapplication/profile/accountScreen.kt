package com.example.chatapplication.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.auth.AuthManager
import com.example.chatapplication.auth.AuthState
import com.example.chatapplication.auth.AuthViewModel
import com.example.chatapplication.navigation.DestinationScreen
import com.example.chatapplication.screens.BottomBar
import com.example.chatapplication.screens.bottomNavItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    authManager: AuthManager
) {
    var expanded by  remember {
        mutableStateOf(false)
    }
    val authState =authManager.authState.collectAsState()
    LaunchedEffect(authState.value) {
        if(authState.value == AuthState.Unauthenticated){navController.navigate(DestinationScreen.signInScreen.route) {
            popUpTo(0) { inclusive = true }
        }


        }
    }

        Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account", color = Color.White) },
                actions = {

                    //  Icon button = dropdown trigger
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Account",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Account") },
                            onClick = {
                                expanded = false
                                navController.navigate(
                                    DestinationScreen.accountScreen.route
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Logout") },
                            onClick = {
                                expanded = false
                                authViewModel.logout()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomBar(bottomNavItems, navController)
        },
        containerColor = Color.Black
    ) { padding ->
            Column(modifier = Modifier.padding(padding)) {

            }

}

//    var expanded by remember { mutableStateOf(false) }
//    val isLoggedIn by authViewModel.authState.collectAsState()
//
//    LaunchedEffect(isLoggedIn) {
//        if (!isLoggedIn) {
//            navController.navigate(DestinationScreen.signInScreen.route) {
//                popUpTo(0) { inclusive = true }
//            }
//        }
//    }


//    }
}
