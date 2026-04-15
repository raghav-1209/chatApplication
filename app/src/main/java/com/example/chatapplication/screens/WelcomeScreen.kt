package com.example.chatapplication.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapplication.R
import com.example.chatapplication.auth.AuthViewModel
import com.example.chatapplication.chats.ChatViewModel
import com.example.chatapplication.models.WholeUser
import com.example.chatapplication.navigation.DestinationScreen
import com.example.chatapplication.profile.FollowerItem
import com.example.chatapplication.profile.ProfileViewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    chatViewModel: ChatViewModel
) {
    val context=LocalContext.current


//    LaunchedEffect(Unit) {
//        profileViewModel.loadUser()
//        profileViewModel.getFollowing()
//    }
//
//    val followingInfo by profileViewModel.FollowingInfo.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Chats",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.clickable{
                    authViewModel.check(context)
                }, tint = Color.White)
            }
        },
        bottomBar = {
            BottomBar(bottomNavItems, navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(DestinationScreen.chatBot.route)
                },
                containerColor = Color.Black
            ) {
                Icon(
                    painter = painterResource(R.drawable.bot),
                    contentDescription = "Start Chat",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { it ->
        Column(modifier=Modifier.padding(it)){

        }

//        LazyColumn(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//                .background(Color.Black),
//            contentPadding = PaddingValues(vertical = 8.dp)
//        ) {
//
//            items(followingInfo) { user ->
//
//                ChatRow(
//                    user = user,
//                    onClick = {
//                        navController.navigate(
//                            DestinationScreen.chatScreen
//                                .chatRoute(user.credentials.uid)
//                        )
//                    },
//                    navController
//                )
//            }
//        }
    }
}
@Composable
fun ChatRow(
    user: WholeUser,
    onClick: () -> Unit,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp) // fixed row height (important!)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = user.image?.takeUnless { it.isEmpty() }
                    ?: R.drawable.user,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .clickable{
                        navController.navigate(DestinationScreen.FullScreenDp.createRoute(user.credentials.uid))
                    }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = user.credentials.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tap to chat",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }

        Divider(
            color = Color.DarkGray.copy(alpha = 0.4f),
            thickness = 0.6.dp,
            modifier = Modifier.padding(start = 88.dp)
        )
    }
}