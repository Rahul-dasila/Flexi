package com.example.flexie.daoEntities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "friend_table")
data class friendsEntity(
    @PrimaryKey
    val id : String,
    val name : String
)
