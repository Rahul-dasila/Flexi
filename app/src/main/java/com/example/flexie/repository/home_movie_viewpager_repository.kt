package com.example.flexie.repository

import com.example.flexie.models.movie_view_pager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class home_movie_viewpager_repository @Inject constructor(private val firebaseStorage: FirebaseStorage, private val firebaseFirestore: FirebaseFirestore) {
    suspend fun getMovieViewpager() : List<movie_view_pager>{
        return try{
            val snapshot = firebaseFirestore.collection("home-movie-view-pager").get().await()
            var moviesVp = snapshot.toObjects(movie_view_pager::class.java)
            for(i in moviesVp){
                i.imageUrl = getImageUri(i.imagePath)
            }
            return moviesVp
        }catch (e : Exception){
            emptyList()
        }
    }



    suspend fun getImageUri(imagePath : String): String{
        return try {
            firebaseStorage.reference.child(imagePath).downloadUrl.await().toString()
        }catch (e : Exception){
            ""
        }
    }
}

