package com.example.flexie.navgraphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flexie.ViewModels.d_homeScreen_ViewModel
import com.example.flexie.screens.BottomNavScreen.Search_screen
import com.example.flexie.screens.BottomNavScreen.chat_screen
import com.example.flexie.screens.BottomNavScreen.d_home_Screen
import com.example.flexie.screens.BottomNavScreen.friends_screen
import com.example.flexie.screens.BottomNavScreen.profile_screen

@Composable
fun BottomNav_navgraph(navController :NavHostController , modifier: Modifier) {
    val d_homeViewModel : d_homeScreen_ViewModel = hiltViewModel()
   NavHost(navController = navController, startDestination = screen.Home.route ){
       composable(screen.Home.route){
         d_home_Screen(d_homeViewModel)
       }
       composable(screen.Search.route){
           Search_screen()
       }
       composable(screen.Chat.route){
           chat_screen()
       }
       composable(screen.Friends.route){
           friends_screen()
       }
       composable(screen.Profile.route){
           profile_screen()
       }
   }

}

sealed class screen(val route : String , val name : String , val icon : ImageVector){
    object Home : screen("home","Home", Icons.Filled.Home)
    object Search : screen("search","Search",Icons.Filled.Search)
    object Chat : screen("chat","Chats",Icons.Filled.Chat)
    object Friends : screen("friends","Friends",Icons.Filled.Groups)
    object Profile :  screen("profile","Profile",Icons.Filled.AccountCircle)
}