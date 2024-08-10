package com.example.flexie.utils

import android.util.Log
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.flexie.MainActivity
import com.example.flexie.ui.theme.Compact
import com.example.flexie.ui.theme.Dimens
import com.example.flexie.ui.theme.ExpandedDimens
import com.example.flexie.ui.theme.Medium
import com.example.flexie.ui.theme.compactMedium
import com.example.flexie.ui.theme.compactSmall

object Dimen{
    var dimen : Dimens = Compact
}
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun dimensAndTypo() {
    val windowSize = calculateWindowSizeClass(activity = LocalContext.current as MainActivity)
    val config = LocalConfiguration.current

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360) {
                logPrint("1")
                Dimen.dimen = compactSmall
            } else if (config.screenWidthDp < 599) {
                logPrint("2")
                Dimen.dimen = compactMedium
            } else{
                logPrint("3")
                Dimen.dimen = Compact
            }
        }

        WindowWidthSizeClass.Medium ->{
            logPrint("4")
            Dimen.dimen = Medium
        }

        WindowWidthSizeClass.Expanded -> {
            logPrint("5")
            Dimen.dimen = ExpandedDimens
        }
    }

}

fun logPrint(msg : String){
    Log.d("rahul",msg)
}