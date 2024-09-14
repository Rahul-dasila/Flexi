package com.example.flexie.repository

import android.util.Log
import com.example.flexie.models.continueWatchingItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ContinueWatchingRepository @Inject constructor(private val fireStore : FirebaseFirestore , private val firebaseAuth: FirebaseAuth , private val storage: FirebaseStorage) {
    suspend fun addToContinueWatching(movieId : String){
        val documentRef = fireStore.collection("users").document(firebaseAuth.currentUser?.uid.toString())
        val item = hashMapOf("continueWatching" to FieldValue.arrayUnion(movieId))

        try{
            val snapshot = documentRef.get().await()
            val list = snapshot.get("continueWatching") as? List<String>?: emptyList()
            if(list.size > 5){
                val updatedList = list.drop(1)
                documentRef.update("continueWatching",updatedList).await()
            }
            documentRef.set(item, SetOptions.merge())
            Log.d("rahul", "Successfully Added")
        }catch (e : Exception){
            Log.d("rahul", e.toString())
        }
    }

    suspend fun getContinueWatching() : List<String>{
        return try {
            val documentRef =
                fireStore.collection("users").document(firebaseAuth.currentUser?.uid.toString())
                    .get().await()
            val list = documentRef.get("continueWatching") as? List<String> ?: emptyList()
            list
        }catch(e :Exception) {
            Log.d("rahul","$e")
            emptyList<String>()
        }
    }

    suspend fun getMovieItem(movieId : List<String>) : List<continueWatchingItem>{
        return try {

            val list : MutableList<continueWatchingItem> = emptyList<continueWatchingItem>().toMutableList()
            movieId.forEachIndexed { index, s ->
                val documentRef =
                    fireStore.collection("movies").whereEqualTo("id", s).get().await()
                val item = documentRef.toObjects(continueWatchingItem::class.java)
                if (item.isNotEmpty()) {
                    item[0].url = getImageUrl(item[0].posterUrl)
                    list.add(item[0])
                }
            }
            list

        }catch (e : Exception){
            Log.d("rahul","so $e")
            emptyList()
        }
    }

    private suspend fun getImageUrl(path : String) : String{
        return try{
            val url = storage.reference.child(path).downloadUrl.await().toString()
            url
        }catch (e : Exception){
            Log.d("rahul" , e.toString())
            ""
        }
    }
}