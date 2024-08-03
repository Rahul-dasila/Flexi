package com.example.flexie.screens

import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.flexie.R
import com.example.flexie.ViewModels.AuthViewmodel
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.utils.setSystemBarColor
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun splashScreen(navController: NavController,authViewmodel: AuthViewmodel,firebaseAuth: FirebaseAuth){
    setSystemBarColor(statusBarColor = darkBlue)
    Box (modifier = Modifier
        .fillMaxSize(1f)
        .background(Color(0xFF00080F)), contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(id = R.drawable.flexie_logo ),
            contentDescription = "Flexie Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .fillMaxHeight(0.2f)
                .align(Alignment.Center) )
    }
    LaunchedEffect(key1 = Unit){
        delay(1000)

        when(val user = firebaseAuth.currentUser) {
             null -> {
                navController.navigate("authentication") {
                    popUpTo(0)
                }
            }
             else -> {
                 Log.d("rahul",user.uid)
                navController.navigate("home_screen") {
                    popUpTo(0)
                }
            }

        }
    }


}