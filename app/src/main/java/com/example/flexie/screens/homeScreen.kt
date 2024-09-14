package com.example.flexie.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
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

    val nv = rememberNavController()
    val navBackStackEntry by nv.currentBackStackEntryAsState()
    // Extract the route from the current back stack entry
    var bottomBarState = rememberSaveable {
        (mutableStateOf(true))
    }
    // Determine if the bottom bar should be visible based on the current route
    when (navBackStackEntry?.destination?.route) {
        "movieDetail/{movieId}" -> {
            bottomBarState.value = false
        }
        "PlayerScreenSolo/{movieId}" -> {
            bottomBarState.value = false
        }
        "full" ->{
            bottomBarState.value = false
        }
        "actualChat/{id}/{name}" ->{
            bottomBarState.value = false
        }
        else -> {
            bottomBarState.value = true
        }
    }

    val context = LocalContext.current
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = bottomNavColor)

    val list = listOf<screen>(
        screen.Home,
        screen.Search,
        screen.Chat,
        screen.Friends,
        screen.Profile
    )

    Scaffold(bottomBar = { if(bottomBarState.value) {BottomNavItems(navController = nv, items =list)} }) {
        BottomNav_navgraph(navController = nv, modifier = Modifier.padding(it))
    }

    if (authViewmodel.isLoggedOut) {
        authViewmodel.isLoggedOut = false
        navController.navigate("authentication") {
            popUpTo(0)
        }
    }

}
