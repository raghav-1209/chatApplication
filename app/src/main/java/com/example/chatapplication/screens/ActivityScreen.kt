package com.example.chatapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapplication.chats.ChatViewModel
import com.example.chatapplication.navigation.DestinationScreen
import com.example.chatapplication.profile.ProfileViewModel

@Composable
fun ActivityScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {

    val users by profileViewModel.AllUsers.collectAsState()
    LaunchedEffect(Unit) {
        profileViewModel.loadAllUsers()
    }
    val followStates by profileViewModel.followStates.collectAsState()

    Scaffold(
        bottomBar = { BottomBar(bottomNavItems, navController) },
        containerColor = Color.Black
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {

            items(users) { user ->

                val uid = user.credentials.uid

                LaunchedEffect(uid) {
                    profileViewModel.followState(uid)
                }

                val state = followStates[uid] ?: "Follow"
                val displayText = when(state) {
                    "NONE" -> "Follow"
                    "REQUESTED" -> "Requested"
                    "REQUEST_RECEIVED" -> "Follows You"
                    "FOLLOWING" -> "Following"
                    "FOLLOWED_BY" -> "Follow Back"
                    else -> "Follow"
                }






                UserItem(
                    name = user.credentials.name,
                    bio = user.bio,
                    image = user.image,
                    followState = displayText,
                    onFollowClick = {
                        profileViewModel.followUser(
                            user.credentials.uid
                        )
                    },
                    onClick = {
                        navController.navigate(DestinationScreen.userProfileScreen.createRoute(user.credentials.uid))
                    }
                )
            }

        }
    }
}
@Composable
fun UserItem(
    name: String,
    bio: String?,
    image: String?,
    onFollowClick: () -> Unit,
    followState: String,
    onClick:()-> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                onClick()
            }
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = image,
                contentDescription = "profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )

                bio?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(onClick = onFollowClick, colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )) {
                Text(followState)

            }
        }
    }
}
@Composable
fun UserProfileScreen(
    uid: String,
    chatViewModel: ChatViewModel,
    navController: NavController,
    profileViewModel: ProfileViewModel
) {

    val user by chatViewModel.User_Info.collectAsState(initial = null)

    LaunchedEffect(uid) {
        chatViewModel.fetchUser(uid)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        user?.let { user ->

            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                AsyncImage(
                    model = user.image,
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .clickable {
                            navController.navigate(
                                DestinationScreen.FullScreenDp.createRoute(
                                    user.credentials.uid
                                )
                            )
                        },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = user.credentials.name,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = user.bio ?: "No bio available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}