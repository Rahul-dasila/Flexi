package com.example.flexie.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun setSystemBarColor(statusBarColor: Color, navigationBarColor: Color = statusBarColor, darkIcons: Boolean = false){
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = darkIcons
        )
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = darkIcons
        )
    }

}
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Activity.setOrientationLandscape(){
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}