package com.example.flexie.repository

import android.util.Log
import com.example.flexie.models.movie_detail_item
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Movie_detail_repository @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore
) {
    suspend fun getMovieDetail(id: String): movie_detail_item? {
        return try {
            val snapshot =
                firebaseFirestore.collection("movies").whereEqualTo("id", id).get().await()
            val obj = snapshot.toObjects(movie_detail_item::class.java)
            if(obj[0].posterUrl != ""){
                obj[0].realPosterUrl = getImageUrl(obj[0].posterUrl)
            }
            obj.getOrNull(0) // Use this instead of a direct return to avoid ArrayIndexOutOfBoundsException
        } catch (e: Exception) {
            Log.d("rahul", e.toString())
            null
        }
    }

    private suspend fun getImageUrl(imagePath : String ) : String{
        return try {
            firebaseStorage.reference.child(imagePath).downloadUrl.await().toString()
        }catch (e : Exception){
            ""
        }
    }
}