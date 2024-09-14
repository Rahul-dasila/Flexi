package com.example.flexie.repository

import com.example.flexie.models.movie_home_row
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {
    suspend fun getRecommendedMovies(): List<movie_home_row> {
        return try {
            val snapshot =
                firebaseFirestore.collection("movies").whereEqualTo("recommended", true).get()
                    .await()
            var list = snapshot.toObjects(movie_home_row::class.java) as List<movie_home_row>
            list = list.shuffled()
            if (list.isNotEmpty()) {
                for (i in list) {
                    i.imageUrI = getUri(i.imagePath)
                }
            }
            return list
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun getUri(path: String): String {
        return try {
            firebaseStorage.reference.child(path).downloadUrl.await().toString()
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun getSearchQuery(query: String): List<movie_home_row> {
        return try {
            val StringBuilder = StringBuilder(query)
            StringBuilder.setCharAt(0, query[0].uppercaseChar())
            var Nquery = StringBuilder.toString()
            val snapshot = firebaseFirestore.collection("movies").orderBy("name").startAt(Nquery)
                .endAt(Nquery + "\uf8ff").get().await()
            val list = snapshot.toObjects(movie_home_row::class.java) as List<movie_home_row>
            if (list.isNotEmpty()) {
                for (i in list) {
                    i.imageUrI = getUri(i.imagePath)
                }
            }
            list
        } catch (e: Exception) {

            emptyList()
        }
    }
}

