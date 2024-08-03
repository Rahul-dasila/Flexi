package com.example.flexie.repository

import android.app.Activity
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.flexie.di.fireBaseModule
import com.example.flexie.models.profile
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth,private val firestore: FirebaseFirestore) {
    fun signInWithPhoneNumber(phoneNumber: String,activity: Activity, callbacks: OnVerificationStateChangedCallbacks) {
        val options = PhoneAuthOptions
            .newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .setActivity(activity)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun SignInWithPhoneAuthCredentials(credential: PhoneAuthCredential) = firebaseAuth.signInWithCredential(credential)


    suspend fun saveUser(uid: String,name : String){
        val user = hashMapOf("name" to name)
        firestore.collection("users").document(uid).set(user).await()
    }

    suspend fun getUserProfile(uid: String):profile? {
        val document = firestore.collection("users").document(uid).get().await()
        return if (document.exists()) {
            document.toObject(profile::class.java)
        } else {
            null
        }
    }
}