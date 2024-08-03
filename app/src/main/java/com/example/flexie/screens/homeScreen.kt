package com.example.flexie.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flexie.ViewModels.AuthViewmodel
import com.example.flexie.navgraphs.BottomNav_navgraph
import com.example.flexie.navgraphs.screen
import com.example.flexie.screens.BottomNavScreen.BottomNavItems
import com.example.flexie.ui.theme.bottomNavColor
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.utils.setSystemBarColor

@Composable
fun homeScreen(authViewmodel: AuthViewmodel, navController: NavController) {
    val context = LocalContext.current
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = bottomNavColor)

    val list = listOf<screen>(
        screen.Home,
        screen.Search,
        screen.Chat,
        screen.Friends,
        screen.Profile
    )
    val nv = rememberNavController()
    Scaffold(bottomBar = { BottomNavItems(navController = nv, items =list) }) {
        BottomNav_navgraph(navController = nv, modifier = Modifier.padding(it))
    }

    if (authViewmodel.isLoggedOut) {
        authViewmodel.isLoggedOut = false
        navController.navigate("authentication") {
            popUpTo(0)
        }
    }

}
