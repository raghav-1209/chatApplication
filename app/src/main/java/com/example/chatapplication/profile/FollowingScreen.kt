package com.example.chatapplication.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun FollowingScreen(profileViewModel: ProfileViewModel,navController: NavController) {

    val followingInfo by profileViewModel.FollowingInfo.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.getFollowing()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Spacer(Modifier.height(10.dp))
        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null, tint = Color.White
            , modifier = Modifier
                .padding(16.dp)
                .clickable {
                    navController.popBackStack()

                })
        Spacer(modifier = Modifier.height(4.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(followingInfo) { users ->

                FollowerItem(
                    name = users.credentials.name,
                    bio = users.bio,
                    image = users.image
                )
            }
        }
    }
}
