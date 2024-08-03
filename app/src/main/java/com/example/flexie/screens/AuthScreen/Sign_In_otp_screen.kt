package com.example.flexie.screens.AuthScreen

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sign_In_otp_screen(navController: NavController, authViewmodel: AuthViewmodel) {
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
                modifier = Modifier
                    .padding(start = 18.dp, bottom = 18.dp, top = 7.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Column {
                Text(
                    text = "Enter the 6 digit code sent to",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.satoshi)),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(18.dp, top = 23.dp)
                )

                Text(
                    text = "+91 ${authViewmodel.mobileNumber}",
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.satoshi)),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(18.dp, top = 8.dp)
                )
            }
            val otpValues = remember { List(6) { mutableStateOf("") } }
            val focusRequesters = List(6) { FocusRequester() }
            val focusManager = LocalFocusManager.current


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp, top = 30.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)  // Spacing between fields
            ) {
                otpValues.forEachIndexed { index, otpValue ->
                    OutlinedTextField(
                        value = otpValue.value,
                        onValueChange = {
                            if (it.length <= 1) {
                                otpValues[index].value =
                                    it.take(1)  // Restrict to a single character
                                if (it.isNotEmpty() && index < otpValues.size - 1) {
                                    focusRequesters[index + 1].requestFocus()  // Move to the next field
                                }
                            }
                        },
                        modifier = Modifier
                            .width(47.dp)
                            .focusRequester(focusRequester = focusRequesters[index])
                            .onKeyEvent { event ->
                                if (event.key == Key.Backspace) {
                                    if (otpValue.value.isEmpty() && index > 0) {
                                        otpValues[index - 1].value = ""
                                        focusRequesters[index - 1].requestFocus()  // Move to previous field
                                        return@onKeyEvent true  // Consume the event
                                    }
                                }
                                false  // Allow other key events to propagate
                            },// Evenly distribute space
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            focusedLabelColor = Color.Gray,
                            focusedTextColor = lightGray2,
                            unfocusedBorderColor = lightGray,
                            unfocusedLabelColor = lightGray,
                            cursorColor = Color.Transparent,
                            errorBorderColor = Color.Red,
                            errorCursorColor = Color.Red,
                            unfocusedTextColor = lightGray2
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next  // Move to the next field on the keyboard
                        )
                    )
                }
            }
            Text(
                text = "An OTP is sent to the number to confirm your mobile number.",
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
                    Text(text = "Didn't receive OTP?  ", color = lightGray2, fontSize = 12.sp)
                    if (authViewmodel.visibilityAndenabled) {
                        Text(
                            text = "Resend OTP",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.clickable {
                                authViewmodel.signInWithPhoneNumber(
                                    "+91${authViewmodel.mobileNumber}",
                                    context as Activity
                                )
                            },
                        )
                    }

                }
                if ((otpValues[0].value + otpValues[1].value + otpValues[2].value + otpValues[3].value + otpValues[4].value + otpValues[5].value).toString()
                        .trim().length == 6  && !authViewmodel.verificationInProgressOtp
                ) {
                    val otp =
                        (otpValues[0].value + otpValues[1].value + otpValues[2].value + otpValues[3].value + otpValues[4].value + otpValues[5].value).toString()
                            .trim()
                    nextBtn2(authViewmodel, otp)
                }
            }
        }
        LaunchedEffect(key1 = authViewmodel.completedResponse) {
            if (authViewmodel.completedResponse) {
                if (authViewmodel.message.isNotEmpty()) {
                    if (authViewmodel.message == "Sign In Successful") {
                        if (authViewmodel.isUserRegistered) {
                            navController.navigate("home_screen"){
                                popUpTo(0)
                            }
                        } else {
                            authViewmodel.message = "completed"
                            navController.navigate("Enter_Name")
                        }
                    }
                }
                // Reset completedResponse to false after handling navigation
                authViewmodel.completedResponse = false
            }
        }

        if (authViewmodel.verificationInProgressOtp) {
            Progressbar()
        }

        LaunchedEffect(key1 = authViewmodel.message){
            if(authViewmodel.message.isNotEmpty() && authViewmodel.message != "completed") {
                Toast.makeText(context, authViewmodel.message, Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(key1 = Unit){
            authViewmodel.visibilityAndenabled = false
            authViewmodel.resendOtp()
        }
    }
}

@Composable
fun nextBtn2(authViewmodel: AuthViewmodel, otp: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                val credentials = PhoneAuthProvider.getCredential(authViewmodel.verificationId, otp)
                authViewmodel.signInWithPhoneAuthCredentials(credentials)
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