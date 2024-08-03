package com.example.flexie.screens.AuthScreen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ehsanmsz.mszprogressindicator.progressindicator.LineSpinFadeLoaderProgressIndicator
import com.example.flexie.R
import com.example.flexie.ViewModels.AuthViewmodel
import com.example.flexie.ui.theme.darkBlue
import com.example.flexie.ui.theme.lightGray
import com.example.flexie.ui.theme.lightGray2
import com.example.flexie.utils.px
import com.example.flexie.utils.setSystemBarColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sign_in_screen(navController: NavController,authViewmodel: AuthViewmodel) {
    setSystemBarColor(statusBarColor = darkBlue, navigationBarColor = lightGray)
    val screenHeight = LocalConfiguration.current.screenHeightDp.px
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(darkBlue, lightGray),
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
                modifier = Modifier.padding(start = 18.dp, bottom = 18.dp, top = 7.dp).clickable {
                    navController.popBackStack()
                }
            )
            Text(
                text = authViewmodel.text1,
                fontFamily = FontFamily(Font(R.font.satoshi)),
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(18.dp, top = 21.dp)
            )
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .border(1.dp, color = lightGray, shape = RoundedCornerShape(8.dp))
                        .padding(top = 15.5.dp, start = 15.5.dp, bottom = 15.5.dp, end = 9.dp)
                        .height(23.5.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                    text = "+91",
                    color = lightGray2,
                    textAlign = TextAlign.End
                )
                Spacer(modifier = Modifier.width(6.dp))
                OutlinedTextField(
                    value = authViewmodel.mobileNumber,
                    onValueChange = { authViewmodel.mobileNumber = it },
                    label = { Text(text = "Mobile number", color = lightGray2) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                    ,
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        focusedTextColor =  Color.Gray,
                        unfocusedBorderColor = lightGray,
                        unfocusedLabelColor = lightGray,
                        cursorColor = Color.Gray,
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        unfocusedTextColor = Color.Gray
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
            Text(
                text = "We'll send you an OTP by SMS to confirm your mobile number.",
                modifier = Modifier.padding(start = 19.dp),
                color = lightGray2,
                fontSize = 10.sp,
            )
            Row(
                modifier = Modifier
                    .padding(end = 20.dp, top = 32.dp, start = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            )
            {
                Row {
                    Text(text = authViewmodel.text2, color = lightGray2, fontSize = 12.sp)
                    Text(
                        text = authViewmodel.text3,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.clickable {
                            authViewmodel.signinOrsignup(!authViewmodel.check)
                        })

                }
                if (authViewmodel.mobileNumber.toString().trim().length == 10 && !authViewmodel.verificationInProgress) {
                    nextBtn(navController,"Sign_in_otp",authViewmodel)
                }
            }
        }

        if(authViewmodel.verificationInProgress){
            Progressbar()
        }
        if (authViewmodel.sentCode) {
            navController.navigate("Sign_in_otp")
            authViewmodel.sentCode = false
            authViewmodel.message = ""
        }

        LaunchedEffect(key1 = authViewmodel.message){
            if (authViewmodel.message.isNotEmpty()) {
                Toast.makeText(context, authViewmodel.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun nextBtn(navController: NavController,route :String,authViewmodel: AuthViewmodel) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                authViewmodel.signInWithPhoneNumber(
                    "+91${authViewmodel.mobileNumber}",
                    context as Activity
                )
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
    }
}

@Composable
fun Progressbar(){
    LineSpinFadeLoaderProgressIndicator(
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center), color = Color.Red,animationDuration = 800, isClockwise = true
    )
}
