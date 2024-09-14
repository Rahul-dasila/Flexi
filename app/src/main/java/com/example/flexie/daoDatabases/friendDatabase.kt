package com.example.flexie.daoDatabases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flexie.dao.friendsDao
import com.example.flexie.daoEntities.friendsEntity

@Database(entities = [friendsEntity::class], version = 1)
abstract class friendDatabase : RoomDatabase() {
    abstract fun friendDao() : friendsDao
}