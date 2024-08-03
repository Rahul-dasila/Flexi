package com.example.flexie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flexie.ViewModels.AuthViewmodel
import com.example.flexie.screens.AuthScreen.EnterNameScreen
import com.example.flexie.screens.AuthScreen.Sign_In_otp_screen
import com.example.flexie.screens.homeScreen
import com.example.flexie.screens.AuthScreen.signUpOrLoginScreen
import com.example.flexie.screens.AuthScreen.sign_in_screen
import com.example.flexie.screens.splashScreen
import com.example.flexie.ui.theme.FlexieTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlexieTheme {
                App(firebaseAuth)
            }
        }
    }
}

@Composable
fun App(firebaseAuth: FirebaseAuth) {
    val authViewModel : AuthViewmodel = hiltViewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable(route = "splash") {
            splashScreen(navController,authViewModel,firebaseAuth)
        }
        composable(route = "authentication") {
            signUpOrLoginScreen(authViewModel,navController)
        }
        composable(route = "Sign_in") {
            sign_in_screen(navController,authViewModel)
        }
        composable(route = "Sign_in_otp") {
            Sign_In_otp_screen(navController,authViewModel)
        }
        composable(route = "Enter_Name"){
            EnterNameScreen(navController = navController, authViewmodel =authViewModel )
        }
        composable(route = "home_screen"){
            homeScreen(authViewModel,navController)
        }
    }

}

