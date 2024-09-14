package com.example.flexie.repository

import android.app.Activity
import android.util.Log
import com.example.flexie.models.profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    fun signInWithPhoneNumber(
        phoneNumber: String,
        activity: Activity,
        callbacks: OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions
            .newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .setActivity(activity)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun SignInWithPhoneAuthCredentials(credential: PhoneAuthCredential) =
        firebaseAuth.signInWithCredential(credential)


    suspend fun saveUser(uid: String, name: String) {
        val user = hashMapOf(
            "name" to name,
            "oneSignalPlayerID" to uid
        )
        firestore.collection("users").document(uid).set(user).await()
    }

    suspend fun getUserProfile(uid: String): profile? {
        val document = firestore.collection("users").document(uid).get().await()
        return if (document.exists()) {
            document.toObject(profile::class.java)
        } else {
            null
        }
    }

    suspend fun saveOneSignalPlayerId(uid: String) {
        try {
            firestore.collection("users").document(uid).update("oneSignalPlayerID", uid)
            Log.e("rakesh", "e.toString()")
        } catch (e: Exception) {
            Log.e("rakesh", e.toString())
        }
    }
}