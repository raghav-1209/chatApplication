package com.example.chatapplication.status

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapplication.models.StatusWithUser
import com.example.chatapplication.navigation.DestinationScreen
import com.example.chatapplication.screens.BottomBar
import com.example.chatapplication.screens.bottomNavItems

@Composable
fun StatusScreen(
    navController: NavController,
    statusViewModel: StatusViewModel
) {

    val statusList by statusViewModel.ImageURL.collectAsState()
    val followingStatus by statusViewModel.FollowingStatus_.collectAsState()

    val grouped = followingStatus.groupBy { it.userId }
    val currUid = statusViewModel.currUid()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            statusViewModel.saveImage(it, currUid)
        }
    }




    val userInfo by statusViewModel.userInfo.collectAsState()
    val converted = statusList.map {
        StatusWithUser(
            userId = currUid,
            name = userInfo?.credentials?.name ?: "You",
            profileImage = userInfo?.image,
            statusImage = it.url,
            createdAt = it.createdAt
        )
    }
    LaunchedEffect(Unit) {
        statusViewModel.getStatusImage()
        statusViewModel.getFollowersStatus()
        statusViewModel.getUserInfo(currUid)
    }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = { BottomBar(bottomNavItems, navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
        ) {

            Text(
                "Status",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(20.dp))

            /* ---------------- MY STATUS ---------------- */

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

                Box {

                    AsyncImage(
                        model = statusList.lastOrNull()?.url ?: userInfo?.image,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .clickable {
                                if (statusList.isNotEmpty()) {
                                    statusViewModel.setSelectedStatuses(converted)
                                    navController.navigate(
                                        DestinationScreen.statusViewerScreen.route
                                    )
                                }
                            },
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "+",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.clickable {
                                launcher.launch("image/*")
                            }
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column {

                    Text(
                        "My Status",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        if (statusList.isEmpty())
                            "Tap + to add status"
                        else
                            "Tap to view your status",
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            /* ---------------- FOLLOWING STATUS ---------------- */

            Text(
                "Recent Updates",
                color = Color.Gray,
                style = MaterialTheme.typography.labelLarge
            )

            Spacer(Modifier.height(10.dp))

            if (grouped.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No recent status from people you follow",
                        color = Color.Gray
                    )
                }

            } else {

                LazyColumn {

                    items(grouped.toList()) { (_, statuses) ->

                        val latest = statuses.last()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {

                                    statusViewModel.setSelectedStatuses(statuses)

                                    navController.navigate(
                                        DestinationScreen.statusViewerScreen.route
                                    )
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = latest.statusImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.width(15.dp))

                            Column {

                                Text(
                                    text = latest.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "Tap to view status",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusViewerScreen(
    statusViewModel: StatusViewModel
) {

    val statusList by statusViewModel.selectedStatuses.collectAsState()
    var index by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        if (statusList.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "No Status Available",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Try again later",
                    color = Color.Gray
                )
            }

        } else {

            /* Image viewer */

            AsyncImage(
                model = "${statusList[index].statusImage}?t=${statusList[index].createdAt}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Fit   // important
            )

            /* Tap navigation areas */

            Row(
                modifier = Modifier.fillMaxSize()
            ) {

                // LEFT SIDE (previous)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            if (index > 0) {
                                index--
                            }
                        }
                )

                // RIGHT SIDE (next)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            if (index < statusList.lastIndex) {
                                index++
                            }
                        }
                )
            }

            /* Progress indicator */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(8.dp)
            ) {

                statusList.forEachIndexed { i, _ ->

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .padding(horizontal = 2.dp)
                            .background(
                                if (i <= index) Color.White else Color.Gray
                            )
                    )
                }
            }
        }
    }
}