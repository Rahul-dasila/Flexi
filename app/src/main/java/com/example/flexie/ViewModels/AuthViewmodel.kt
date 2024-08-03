package com.example.flexie.ViewModels

import android.app.Activity
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexie.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.logging.Handler
import javax.inject.Inject

@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val AuthRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    var verificationInProgress by mutableStateOf(false)
    var message by mutableStateOf("")
    var verificationId by mutableStateOf("")
    var mobileNumber by mutableStateOf("")
    var sentCode by mutableStateOf(false)
    var verificationInProgressOtp by mutableStateOf(false)
    var name by mutableStateOf("")
    var isUserRegistered by mutableStateOf(false)
    var text1 by mutableStateOf("")
    var text2 by mutableStateOf("")
    var text3 by mutableStateOf("")
    var check by mutableStateOf(false)
    var visibilityAndenabled by mutableStateOf(false)
    var completedResponse by mutableStateOf(false)
    var completedResponse2 by mutableStateOf(false)
    var clicked by mutableStateOf(false)
    var isLoggedOut by mutableStateOf(false)





    fun signInWithPhoneNumber(phoneNumber: String, activity: Activity) {
        verificationInProgress = true
        AuthRepository.signInWithPhoneNumber(
            phoneNumber,
            activity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    verificationInProgress = false
                    message = "Verification Completed"
                    signInWithPhoneAuthCredentials(p0)
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    verificationInProgress = false
                    message = "Verification Failed: ${p0.message}"
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    verificationInProgress = false
                    message = "Otp sent"
                    visibilityAndenabled = false
                    sentCode = true
                    this@AuthViewmodel.verificationId = verificationId
                }
            })
    }


    fun signInWithPhoneAuthCredentials(credentials: PhoneAuthCredential) {
        message = ""
        completedResponse = false
        viewModelScope.launch {
            verificationInProgressOtp = true
            AuthRepository.SignInWithPhoneAuthCredentials(credentials)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        message = "Sign In Successful"
                        val user = firebaseAuth.currentUser
                        user?.let {
                            checkIfUserRegisteredOrNot(user.uid)
                        }
                    } else {
                        verificationInProgressOtp = false
                        message = "Sign In Failed: ${task.exception?.message}"
                        completedResponse = false
                    }
                }
        }
    }


    private fun checkIfUserRegisteredOrNot(uid: String) {
        viewModelScope.launch {
            val userProfile = AuthRepository.getUserProfile(uid)
            isUserRegistered = userProfile?.name?.isNotEmpty() ?: false
            completedResponse = true
            verificationInProgressOtp = false
        }
    }

    fun logout() {
        isLoggedOut = false
        viewModelScope.launch {
            try {
                firebaseAuth.signOut() // Sign out from Firebase
                isLoggedOut = true
            } catch (e: Exception) {
                // Handle sign-out error (e.g., log it or show a message)
                Log.e("AuthViewModel", "Logout failed", e)
            }
        }
    }

    fun saveUser(name: String) {
        completedResponse2 = true
        firebaseAuth.currentUser?.uid?.let {
            viewModelScope.launch {
                AuthRepository.saveUser(it, name)
                completedResponse2 = false
            }
        }
    }

    fun signinOrsignup(check: Boolean) {
        this.check = check
        if (check) {
            text1 = "Sign up to continue"
            text2 = "Are you already a user?  "
            text3 = "Sign In"
        } else {
            text1 = "Sign in to continue"
            text2 = "Create a new account?  "
            text3 = "Sign Up"
        }
    }

    fun resendOtp() {
        android.os.Handler(Looper.myLooper()!!).postDelayed(
            Runnable {
                visibilityAndenabled = true
            }, 60000
        )
    }



//    For Learning purpose

//    sealed class AuthState {
//        data class Authenticated(val uid: String) : AuthState()
//        object Unauthenticated : AuthState()
//    }
    
}