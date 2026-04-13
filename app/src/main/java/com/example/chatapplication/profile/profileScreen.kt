package com.example.chatapplication.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SettingsPower
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapplication.R
import com.example.chatapplication.auth.AuthViewModel
import com.example.chatapplication.navigation.DestinationScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel
) {

//    val followerCount by profileViewModel.followerCount.collectAsState()
//    val followingCount by profileViewModel.FollowingCount.collectAsState()
//    val user by profileViewModel.wholeUser.collectAsState()
//
//    var name by remember { mutableStateOf("") }
//    var bioText by remember { mutableStateOf("") }
//    var isEditingBio by remember { mutableStateOf(false) }
//    var image by remember { mutableStateOf("") }
//    LaunchedEffect(Unit) {
//        profileViewModel.getFollowers()
//        profileViewModel.getFollowing()
//    }
//
//    LaunchedEffect(user) {
//        user?.let {
//            name = it.credentials.name
//            bioText = it.bio ?: ""
//            image = it.image ?: ""
//        }
//    }
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri ->
//        uri?.let { profileViewModel.saveImage(it) }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//    ) {
//
//        TopAppBar(
//            title = {Text("Account",color=Color.White)},
//            navigationIcon = {
//                IconButton(onClick = {
//                    navController.popBackStack()
//                }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color.Black
//            )
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//
//            Spacer(modifier = Modifier.height(60.dp))
//
//            Box(contentAlignment = Alignment.BottomEnd) {
//
//                AsyncImage(
//                    model = image.takeIf { it.isNotBlank() },
//                    contentDescription = "Profile Image",
//                    contentScale = ContentScale.Crop,
//                    placeholder = painterResource(R.drawable.user),
//                    error = painterResource(R.drawable.user),
//                    modifier = Modifier
//                        .size(150.dp)
//                        .clip(CircleShape)
//                        .border(
//                            3.dp,
//                            Color.DarkGray,
//                            CircleShape
//                        )
//                )
//
//                Box(
//                    modifier = Modifier
//                        .size(36.dp)
//                        .clip(CircleShape)
//                        .background(Color.DarkGray)
//                        .clickable { launcher.launch("image/*") },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = "Change Photo",
//                        tint = Color.White
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            OutlinedTextField(
//                value = name,
//                onValueChange = { name = it },
//                singleLine = true,
//                label = { Text("Name") },
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color.DarkGray,
//                    unfocusedBorderColor = Color.Gray,
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    cursorColor = Color.DarkGray
//                )
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 🔥 Bio
//            OutlinedTextField(
//                value = bioText,
//                onValueChange = { bioText = it },
//                label = { Text("Bio") },
//                maxLines = 4,
//                readOnly = !isEditingBio,
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(16.dp),
//                trailingIcon = {
//                    Icon(
//                        imageVector = if (isEditingBio) Icons.Default.Check else Icons.Default.Edit,
//                        contentDescription = null,
//                        tint = Color.DarkGray,
//                        modifier = Modifier.clickable {
//                            if (isEditingBio) {
//                                profileViewModel.saveBio(bioText)
//                            }
//                            isEditingBio = !isEditingBio
//                        }
//                    )
//                },
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color.DarkGray,
//                    unfocusedBorderColor = Color.Gray,
//                    focusedTextColor = Color.White,
//                    unfocusedTextColor = Color.White,
//                    cursorColor = Color.DarkGray
//                )
//            )
//
//            Spacer(modifier = Modifier.height(30.dp))
//
//            Card (
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color(0xFF252525)
//                ),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Row(
//                    modifier = Modifier
//                        .padding(vertical = 20.dp)
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.clickable {
//                            navController.navigate(DestinationScreen.followerScreen.route)
//                        }
//                    ) {
//                        Text(
//                            text = followerCount.toString(),
//                            fontSize = 22.sp,
//                            color = Color.White
//                        )
//                        Text(
//                            text = "Followers",
//                            fontSize = 14.sp,
//                            color = Color.Gray
//                        )
//                    }
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier.clickable {
//                            navController.navigate(DestinationScreen.followingScreen.route)
//                        }
//                    ) {
//                        Text(
//                            text = followingCount.toString(),
//                            fontSize = 22.sp,
//                            color = Color.White
//                        )
//                        Text(
//                            text = "Following",
//                            fontSize = 14.sp,
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Button(
//                onClick = {
//                    authViewModel.logout()
//                    navController.navigate(DestinationScreen.signInScreen.route)
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Gray
//                ),
//                shape = RoundedCornerShape(16.dp),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 40.dp)
//            ) {
//                Icon(Icons.Default.SettingsPower, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Logout")
//            }
//        }
//    }
}