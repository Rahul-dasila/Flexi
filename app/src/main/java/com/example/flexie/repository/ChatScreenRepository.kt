package com.example.flexie.repository

import android.util.Log
import com.example.flexie.dao.friendsDao
import com.example.flexie.daoEntities.friendsEntity
import com.example.flexie.models.User
import com.example.flexie.models.friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatScreenRepository @Inject constructor(private val firestore: FirebaseFirestore , private val firebaseStorage: FirebaseStorage ,private val firebaseAuth: FirebaseAuth , private val friendsDao: friendsDao) {

    suspend fun getAllfriends() : Flow<List<friendsEntity>> {
        return friendsDao.getFriends()
    }
    suspend fun startFriendListener(){
        firestore.collection("friends").document(firebaseAuth.currentUser!!.uid).addSnapshotListener { value, error ->
            if (error != null){
                Log.d("daoError" , error.toString())
                return@addSnapshotListener
            }
            if(value != null){
                var list = value.toObject(friend::class.java)
                if(list == null){
                    list = friend(friendList = emptyList())
                }
                CoroutineScope(Dispatchers.IO).launch {
                    var users  = mutableListOf<friendsEntity>()
                    for(i in list.friendList){
                        val user = getUserDetails(i)
                        if(user!=null){
                            users.add(friendsEntity(user.oneSignalPlayerID , user.name))
                        }
                    }
                    friendsDao.clearFriends()
                    friendsDao.insertFriends(users)
                }
            }
        }
    }

    private suspend fun getUserDetails(id : String) : User?{
        return try {
            val snapshot = firestore.collection("users").document(id).get().await()
            var user = snapshot.toObject(User::class.java)
            user
        }catch (e : Exception){
            Log.d("userError" ,e.toString())
            null
        }
    }
}