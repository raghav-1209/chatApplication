package com.example.chatapplication.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chatapplication.auth.AuthViewModel
import com.example.chatapplication.auth.LoginScreen
import com.example.chatapplication.auth.SignInScreen
import com.example.chatapplication.chats.ChatBotScreen
import com.example.chatapplication.chats.ChatScreen
import com.example.chatapplication.chats.ChatViewModel
import com.example.chatapplication.profile.AccountScreen
import com.example.chatapplication.profile.FollowerScreen
import com.example.chatapplication.profile.FollowingScreen
import com.example.chatapplication.profile.FullScreenDp
import com.example.chatapplication.profile.ProfileViewModel
import com.example.chatapplication.screens.splashScreen
import com.example.chatapplication.status.StatusScreen
import com.example.chatapplication.profile.ProfileScreen
import com.example.chatapplication.screens.ActivityScreen
import com.example.chatapplication.screens.UserProfileScreen
import com.example.chatapplication.screens.WelcomeScreen
import com.example.chatapplication.status.StatusViewModel
import com.example.chatapplication.status.StatusViewerScreen

sealed class DestinationScreen (val route: String){
    object loginScreen: DestinationScreen("loginScreen")
    object signInScreen: DestinationScreen("signInScreen")
    object splashScreen: DestinationScreen("splashScreen")
    object welcomeScreen: DestinationScreen("welcomeScreen")
    object profileScreen: DestinationScreen("profileScreen")
    object statusScreen: DestinationScreen("statusScreen")

    object accountScreen: DestinationScreen("accountScreen")
    object activityScreen: DestinationScreen("activityScreen")
    object followerScreen: DestinationScreen("followScreen")
    object followingScreen: DestinationScreen("followingScreen")
    object chatScreen: DestinationScreen("ChatScreen/{following_Uid}"){
        fun chatRoute(following_Uid: String) = "ChatScreen/$following_Uid"
    }
    object chatBot: DestinationScreen("chatBot")
    object userProfileScreen: DestinationScreen("userProfileScreen/{uid}") {
        fun createRoute(uid: String) = "userProfileScreen/$uid"
    }
    object FullScreenDp: DestinationScreen("FullScreenDp/{uid}") {
        fun createRoute(uid: String) = "FullScreenDp/$uid"
    }
    object statusViewerScreen: DestinationScreen("statusViewerScreen")








}
@Composable
fun navFlow(onSplash:()->Unit){
    val statusViewModel: StatusViewModel = hiltViewModel()

    val navController= rememberNavController()
    NavHost(navController, startDestination = DestinationScreen.splashScreen.route){
        composable(DestinationScreen.splashScreen.route){
            splashScreen(onSplash = {onSplash()},navController)
        }
        composable(DestinationScreen.loginScreen.route){
            val viewmodel: AuthViewModel= hiltViewModel()
            val profileViewModel: ProfileViewModel= hiltViewModel()


            LoginScreen(viewmodel,navController,profileViewModel)
        }
        composable(DestinationScreen.signInScreen.route){
            val viewmodel: AuthViewModel= hiltViewModel()
            SignInScreen(viewmodel,navController)
        }
        composable(DestinationScreen.welcomeScreen.route){
            val viewmodel: AuthViewModel= hiltViewModel()
            val profileViewModel: ProfileViewModel= hiltViewModel()
            val chatViewModel: ChatViewModel= hiltViewModel()


            WelcomeScreen(navController,viewmodel,profileViewModel,chatViewModel)
        }
        composable(DestinationScreen.profileScreen.route) {
            val viewModel: ProfileViewModel=hiltViewModel()
            val authViewModel: AuthViewModel=hiltViewModel()


            ProfileScreen(navController,viewModel,authViewModel)
        }
        composable(DestinationScreen.accountScreen.route) {
            val viewmodel: AuthViewModel= hiltViewModel()

            AccountScreen(navController,viewmodel)
        }
        composable(DestinationScreen.statusScreen.route) {


            StatusScreen(navController,statusViewModel)
        }
        composable(DestinationScreen.activityScreen.route) {
            val viewModel: ProfileViewModel=hiltViewModel()

            ActivityScreen(navController,viewModel)
        }
        composable(DestinationScreen.followerScreen.route) {
            val viewModel: ProfileViewModel=hiltViewModel()

            FollowerScreen(viewModel,navController)
        }
        composable(DestinationScreen.followingScreen.route) {
            val viewModel: ProfileViewModel=hiltViewModel()

            FollowingScreen(viewModel,navController)
        }
        composable(
            route = DestinationScreen.chatScreen.route,
            arguments = listOf(
                navArgument("following_Uid") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val chatViewModel: ChatViewModel = hiltViewModel()
            val profileViewModel: ProfileViewModel = hiltViewModel()


            val uid = backStackEntry.arguments?.getString("following_Uid") ?: ""

            ChatScreen(uid, chatViewModel,profileViewModel,navController)
        }
        composable(DestinationScreen.chatBot.route){
            val chatViewModel: ChatViewModel = hiltViewModel()

            ChatBotScreen(chatViewModel,navController)
        }
        composable(
            route = DestinationScreen.userProfileScreen.route,
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val chatViewModel: ChatViewModel = hiltViewModel()
            val profileViewModel: ProfileViewModel = hiltViewModel()


            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            UserProfileScreen(uid, chatViewModel,navController,profileViewModel)
        }
        composable(
            route = DestinationScreen.FullScreenDp.route,
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val chatViewModel: ChatViewModel = hiltViewModel()


            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            FullScreenDp(uid,chatViewModel)
        }
        composable(DestinationScreen.statusViewerScreen.route){
            StatusViewerScreen(   statusViewModel = statusViewModel)
        }




    }
}