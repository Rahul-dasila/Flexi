package com.example.flexie.screens.AuthScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flexie.R
import com.example.flexie.ViewModels.AuthViewmodel
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.lightGray
import com.example.flexie.ui.theme.lightGray2
import com.example.flexie.utils.px
import com.example.flexie.utils.setSystemBarColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterNameScreen(navController: NavController, authViewmodel: AuthViewmodel) {
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = lightGray)
    val screenHeight = LocalConfiguration.current.screenHeightDp.px
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        darkBlue, lightGray
                    ),
                    startY = 0f,
                    endY = screenHeight / 0.55f
                )
            )
            .padding(top = 15.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "backBtn",
                modifier = Modifier
                    .padding(start = 18.dp, bottom = 18.dp, top = 7.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Text(
                text = "It seems like you are new here,", color = Color.White,
                fontFamily = FontFamily(Font(R.font.satoshi)),
                fontSize = 15.sp,
                modifier = Modifier.padding(18.dp, top = 23.dp)
            )
            Text(
                text = "Enter your name",
                modifier = Modifier.padding(start = 18.dp, top = 7.dp),
                color = Color.White,
                fontFamily = FontFamily(
                    Font(R.font.satoshi)
                ), fontSize = 20.sp
            )

            OutlinedTextField(
                value = authViewmodel.name,
                onValueChange = { authViewmodel.name = it },
                modifier = Modifier
                    .padding(start = 18.dp, top = 26.dp, bottom = 20.dp, end = 20.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                label = {
                    Text(text = "Name", color = lightGray2)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    focusedTextColor = Color.Gray,
                    unfocusedBorderColor = lightGray,
                    unfocusedLabelColor = lightGray,
                    cursorColor = Color.Gray,
                    errorBorderColor = Color.Red,
                    errorCursorColor = Color.Red,
                    unfocusedTextColor = Color.Gray
                )

            )
            Text(
                text = "Don't worry you can also change your name later.",
                modifier = Modifier.padding(start = 19.dp),
                color = lightGray2,
                fontSize = 10.sp,
            )

            Row(
                modifier = Modifier
                    .padding(end = 20.dp, top = 32.dp, start = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                if (authViewmodel.name.isNotEmpty()) {
                    nextbtnEnter(authViewmodel, navController)
                }
            }


        }
        if (authViewmodel.completedResponse2) {
            Progressbar()
        }
    }
}

@Composable
fun nextbtnEnter(authViewmodel: AuthViewmodel, navController: NavController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                authViewmodel.saveUser(authViewmodel.name)
                authViewmodel.clicked = true
            }
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Red, Color.Transparent
                    ), startX = 0f,
                    endX = 150f
                )
            )
            .padding(11.dp)

    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_arrow_forward_24),
            contentDescription = "go"
        )

        LaunchedEffect(key1 = authViewmodel.completedResponse2) {
            if (!authViewmodel.completedResponse2 && authViewmodel.clicked) {
                navController.navigate("home_screen") {
                    popUpTo(0)
                }
            }
        }
    }
}
