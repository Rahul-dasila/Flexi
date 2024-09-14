package com.example.flexie.daoDatabases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flexie.dao.conversationDao
import com.example.flexie.daoEntities.conversation

@Database(entities = [conversation::class] , version = 1)
abstract class conversationDatabase : RoomDatabase() {
    abstract fun conversationDao() : conversationDao
}
