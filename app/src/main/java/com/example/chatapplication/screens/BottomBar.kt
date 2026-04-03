package com.example.chatapplication.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.chatapplication.navigation.DestinationScreen

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home, DestinationScreen.welcomeScreen.route),
    BottomNavItem("Status", Icons.Filled.AddCircle, DestinationScreen.statusScreen.route),
    BottomNavItem("Activity", Icons.Filled.Notifications, DestinationScreen.activityScreen.route),

    BottomNavItem("Profile", Icons.Filled.Person, DestinationScreen.profileScreen.route)

)

@Composable
fun BottomBar(
    items: List<BottomNavItem>,
    navController: NavController
) {
    var selectedIndex by remember { mutableStateOf(0) } // internal state

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .height(60.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable {
                   navController.navigate(item.route)
                        selectedIndex = index
                    }
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (index == selectedIndex) Color.White else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = item.label,
                    color = if (index == selectedIndex) Color.White else Color.Gray
                )
            }
        }
    }
}

