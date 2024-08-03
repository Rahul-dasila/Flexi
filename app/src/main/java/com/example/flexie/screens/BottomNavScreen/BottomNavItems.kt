package com.example.flexie.screens.BottomNavScreen

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flexie.navgraphs.screen
import com.example.flexie.ui.theme.bottomNavColor

@Composable
fun BottomNavItems(navController: NavHostController, items: List<screen>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    BottomNavigation(backgroundColor = bottomNavColor) {
        items.forEach { screen ->
            val select = currentRoute == screen.route
            BottomNavigationItem(
                selected = select,
                onClick = {

                    if (currentRoute != screen.route) {
                        if(screen.route == "home"){
                            navController.navigate(screen.route){
                                popUpTo(0)
                            }
                        }else {
                            navController.navigate(screen.route)
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = null,
                        tint = if (select) Color.White else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = screen.name,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.SansSerif
                        ,
                        color = if (select) Color.White else Color.Gray
                    )
                }
            )
        }
    }
}