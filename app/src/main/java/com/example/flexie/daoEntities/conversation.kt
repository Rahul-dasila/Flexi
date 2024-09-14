package com.example.flexie.daoEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Conversation")
data class conversation(
    @PrimaryKey val id : String,
    val senderId : String,
    val receiverId :String,
    val text : String,
    val timestamp: Long
)
