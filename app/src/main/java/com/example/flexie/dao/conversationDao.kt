package com.example.flexie.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flexie.daoEntities.conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface conversationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addConversation(conversation: conversation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addConversations(conversations : List<conversation>)

    @Query("SELECT * FROM conversation WHERE senderId = :id OR receiverId = :id ORDER BY timestamp ASC ")
    fun getMessages(id: String) : Flow<List<conversation>>

    @Query("DELETE FROM conversation WHERE senderId = :id OR receiverId = :id")
    suspend fun deleteConversation(id : String)
}