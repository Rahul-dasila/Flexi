package com.example.flexie.repository

import android.util.Log
import com.example.flexie.models.IncludeAliases
import com.example.flexie.models.currentUser
import com.example.flexie.models.friend
import com.example.flexie.models.friendRequest
import com.example.flexie.models.notification
import com.example.flexie.models.searchPeopleItem
import com.example.flexie.services.OneSignalService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class AddPeopleRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val service: OneSignalService,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun searchPeople(query: String): List<searchPeopleItem> {
        return try {
            val snapshot =
                firestore.collection("users").orderBy("name").startAt(query).endAt(query + "\uf8ff")
                    .get().await()

            val list = snapshot.toObjects(searchPeopleItem::class.java)
            list
        } catch (e: Exception) {
            Log.d("projectErrors", e.toString())
            emptyList()
        }
    }

    suspend fun getCurrentUser(): currentUser {
        val snapShot =
            firestore.collection("users").document(firebaseAuth.currentUser!!.uid).get().await()
        val user = snapShot.toObject(currentUser::class.java)
        return user!!
    }

    suspend fun sendNotification(oneSignalId: String, name: String): Response<Unit> {
        val notification = notification(
            app_id = "521ec99d-5951-4f6a-8214-f80675e34ced",
            headings = mapOf("en" to "Friend Request"),
            contents = mapOf("en" to "$name sent you a friend request."),
            data = mapOf("type" to "friend_request"),
            include_aliases = IncludeAliases(
                external_id = listOf(
                    oneSignalId
                )
            )
        )
        val response = service.sendNotification(notification)
        return response
    }

    suspend fun getFriendRequestsSender(receiverID: String): List<friendRequest> {
        return try {
            val snapshot = firestore.collection("friendRequests")
                .whereEqualTo("sender", firebaseAuth.currentUser!!.uid)
                .whereEqualTo("receiver", receiverID).get().await()
            val list = snapshot.toObjects(friendRequest::class.java)
            Log.e("rahulD", list.toString())
            list
        } catch (e: Exception) {
            Log.e("rahulD", e.toString())
            emptyList()
        }
    }

    suspend fun getFriendRequestsReceiver(senderId: String): List<friendRequest> {
        return try {
            val snapshot = firestore.collection("friendRequests")
                .whereEqualTo("receiver", firebaseAuth.currentUser!!.uid)
                .whereEqualTo("sender", senderId).get().await()
            val list = snapshot.toObjects(friendRequest::class.java)
            list
        } catch (e: Exception) {
            Log.e("rahul", e.toString())
            emptyList()
        }
    }

    suspend fun getFriends(): friend {
        return try {
            val snapshot =
                firestore.collection("friends").document(firebaseAuth.currentUser!!.uid).get()
                    .await()
            var item = snapshot.toObject(friend::class.java)
            if (item == null) {
                item = friend(friendList = emptyList())
            }
            Log.e("rahulk", snapshot.toString())
            return item
        } catch (e: Exception) {
            Log.e("rahulk", e.toString())
            val item = friend(friendList = emptyList())
            item
        }
    }

    suspend fun sendFriendRequest(id: String): Boolean {
        return try {
            var friendRequest = friendRequest(
                sender = firebaseAuth.currentUser!!.uid,
                receiver = id,
                status = false,
                timeStamp = Timestamp.now()
            )
            firestore.collection("friendRequests").add(friendRequest).await()
            true
        } catch (e: Exception) {
            Log.e("rahul", e.toString())
            false
        }
    }

    suspend fun addFriend(id: String) {
        var value = false
        val ref = firestore.collection("friends").document(firebaseAuth.currentUser!!.uid)
        val ref2 = firestore.collection("friends").document(id)
        val data1 = mapOf("friendList" to FieldValue.arrayUnion(id))
        val data2 = mapOf("friendList" to FieldValue.arrayUnion(firebaseAuth.currentUser!!.uid))
        val converstionRef = firestore.collection("conversations").document()
        val members = hashMapOf("members" to listOf(firebaseAuth.currentUser!!.uid , id))
        firestore.runBatch {
            ref.set(data1, SetOptions.merge())
            ref2.set(data2, SetOptions.merge())
            converstionRef.set(members)
        }.addOnSuccessListener {
            value = true
            val currentUserId = firebaseAuth.currentUser!!.uid

            firestore.collection("friendRequests")
                .whereEqualTo("sender", id)
                .whereEqualTo("receiver", currentUserId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot) {
                        // Get the document ID
                        val docId = document.id

                        // Delete the document using its ID
                        firestore.collection("friendRequests").document(docId)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("rahul" , "request deleted")
                            }
                            .addOnFailureListener { e ->
                                // Handle the error
                                Log.d("rahul" , "request deleted error ${e.message}")
                            }
                    }

                    // Check if no documents matched the query
                    if (querySnapshot.isEmpty) {
                        Log.d("rahul" , "no doc found")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the error when querying the documents
                    Log.d("rahul" , "some error ${e.message}")
                }
        }.addOnFailureListener {
            Log.e("rahul", "add krte time ye error aara h ${it.toString()}")
            value = false
        }
    }
}