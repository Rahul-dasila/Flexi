package com.example.flexie.repository

import android.util.Log
import com.example.flexie.dao.conversationDao
import com.example.flexie.daoEntities.conversation
import com.example.flexie.models.IncludeAliases
import com.example.flexie.models.User
import com.example.flexie.models.messages
import com.example.flexie.models.notification
import com.example.flexie.services.OneSignalService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import javax.inject.Inject

class ActualChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth,
    private val conversationDao: conversationDao,
    private val service: OneSignalService
) {
    suspend fun getChats(id: String) {
        firestore.collection("conversations")
            .whereArrayContains("members", firebaseAuth.currentUser!!.uid)
            .get().addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {

                    val messagesDoc = querySnapshot.documents.firstOrNull {
                        Log.d("flr", it.toString())
                        val list = it["members"] as? List<*>
                        list?.contains(id) == true
                    }

                    messagesDoc!!.reference.collection("messages")
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                Log.d("getChaterror", error.toString())
                                return@addSnapshotListener
                            }
                            if (value != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    val messagesLst = mutableListOf<conversation>()

                                    // Iterate over each document in the QuerySnapshot
                                    for (document in value.documents) {
                                        // Get the message object and the document ID
                                        val message = document.toObject(messages::class.java)
                                        val documentId = document.id

                                        if (message != null) {
                                            // Create a conversation object including the document ID if needed
                                            val msg = conversation(
                                                id = documentId,
                                                senderId = message.sender,
                                                receiverId = message.receiver,
                                                text = message.text,
                                                timestamp = message.timestamp!!.toDate().time
                                            )

                                            // If you want to use the document ID, you can do something with it here
                                            Log.d("DocumentID", "Message ID: $documentId")

                                            messagesLst.add(msg)
                                        }
                                    }

                                    // Insert messages into the Room database
//                                    conversationDao.deleteConversation(id) // Adjust this logic as needed
                                    conversationDao.addConversations(messagesLst)
                                }
                            }
                        }
                }
            }
            .addOnFailureListener {
                Log.d("flr", it.message.toString())
            }
    }

    suspend fun sendMessage(text: String, id: String , name:String) {
        Log.d("mohan", "2.5")
        firestore.collection("conversations")
            .whereArrayContains("members", firebaseAuth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    val conversationDoc = it.documents.firstOrNull { document ->
                        val members = document["members"] as? List<*>
                        members?.contains(id) == true
                    }
                    if (conversationDoc != null) {
                        Log.d("mohan", "3")
                        // Create the message object
                        val message = messages(
                            sender = firebaseAuth.currentUser!!.uid,
                            receiver = id,
                            timestamp = Timestamp.now(),
                            text = text
                        )

                        // Reference to the messages collection within the conversation
                        conversationDoc.reference.collection("messages").add(message)
                            .addOnSuccessListener {
                                Log.d("sentOrNot", "Sent")
                                CoroutineScope(Dispatchers.IO).launch {
                                    sendNotification(id , text , name)
                                }
                            }
                            .addOnFailureListener { error ->
                                Log.e("sentOrNot", error.message.toString())
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.d("mohan", it.message.toString())
            }
    }

    suspend fun getText(id: String): Flow<List<conversation>> {
        return conversationDao.getMessages(id)
    }

    suspend fun sendNotification(oneSignalId: String, msg: String , name: String): Response<Unit> {
        val notification = notification(
            app_id = "521ec99d-5951-4f6a-8214-f80675e34ced",
            headings = mapOf("en" to name),
            contents = mapOf("en" to msg),
            data = mapOf("type" to "New Message"),
            include_aliases = IncludeAliases(
                external_id = listOf(
                    oneSignalId
                )
            )
        )
        val response = service.sendNotification(notification)
        return response
    }


     suspend fun getUserDetails(id : String) : String?{
        return try {
            val snapshot = firestore.collection("users").document(id).get().await()
            var user = snapshot.toObject(User::class.java)
            user?.name
        }catch (e : Exception){
            Log.d("userError" ,e.toString())
            null
        }
    }
}