package com.example.flexie.screens.AuthScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flexie.R
import com.example.flexie.ViewModels.AuthViewmodel
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.utils.Dimen
import com.example.flexie.utils.px
import com.example.flexie.utils.setSystemBarColor

@Composable
fun signUpOrLoginScreen(authViewmodel: AuthViewmodel, navController: NavController) {
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = Color.Black)
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.initial_image),
            contentDescription = "initial",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = 0f,
                        endY = 400f
                    )
                )
        )
        val screenHeight = LocalConfiguration.current.screenHeightDp.px
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 0f,
                        endY = screenHeight / 1.4f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
            , verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Unlimited Entertainment",
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.titleLarge,
                fontSize = Dimen.dimen.fontSizeLarge.sp, textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.satoshi))
            )
            Spacer(modifier = Modifier.size(7.dp))
            Text(
                text = "Movie Magic",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                fontSize = Dimen.dimen.fontSizeLarge.sp, textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.satoshi))
            )
            Text(
                text = "Watch, enjoy, repeat",
                color = Color.Gray,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(13.dp),
                fontSize = Dimen.dimen.fontSizeMedium.sp, textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.satoshi))
            )
            Box(
                modifier = Modifier
                    .padding(top = 35.dp)
                    .background(Color.Red, shape = RoundedCornerShape(14.dp))
                    .fillMaxWidth(0.75f)
                    .wrapContentHeight()
                    .clickable {
                        authViewmodel.signinOrsignup(false)
                        navController.navigate("Sign_in")
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sign In",
                    fontFamily = FontFamily(Font(R.font.satoshi)),
                    color = Color.White,
                    fontSize = Dimen.dimen.fontSizeMedium.sp,
                    modifier = Modifier.padding(Dimen.dimen.paddingMedium)
                )
            }
            Spacer(modifier = Modifier.size(7.dp))
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Text(text = "Create a new account?  ", color = Color.Gray, fontSize = Dimen.dimen.fontSizeSmall.sp)
                Text(
                    text = "Sign Up",
                    color = Color.Red,
                    fontSize = Dimen.dimen.fontSizeSmall.sp,
                    modifier = Modifier.clickable {
                        authViewmodel.signinOrsignup(true)
                        navController.navigate("Sign_in")
                    })
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}