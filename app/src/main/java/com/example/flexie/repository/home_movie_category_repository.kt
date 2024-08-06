package com.example.flexie.repository

import android.util.Log
import com.example.flexie.models.movie_categories
import com.example.flexie.models.movie_home_row
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class home_movie_category_repository @Inject constructor(private val firestore: FirebaseFirestore , private val firebaseStorage: FirebaseStorage) {

    suspend fun getMovieCategory() : List<movie_categories>{
        return try{
            val snapshot = firestore.collection("movie_categories").get().await()
            return snapshot.toObjects(movie_categories::class.java)
        }catch (e : Exception){
            Log.d("rahul","$e")
            emptyList()
        }
    }

    suspend fun getMovies(type : String) : List<movie_home_row>{
        return try {
            val snapshot = firestore.collection("movies").whereArrayContains("category" ,type).get().await()
            val list =  snapshot.toObjects(movie_home_row::class.java)
            for(i in list){
                i.imageUrI = getImageuri(i.imagePath)
            }
            return list
        }catch (e : Exception){
            Log.d("rahul","$e")
            emptyList()
        }
    }

    suspend fun getImageuri(imagePath : String): String{
        return try{
            firebaseStorage.reference.child(imagePath).downloadUrl.await().toString()
        }catch (e : Exception){
            Log.d("rahul","$e")
            ""
        }
    }
}