package com.example.flexie.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flexie.daoEntities.friendsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface friendsDao {
    @Query("SELECT * FROM friend_table")
    fun getFriends() : Flow<List<friendsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends : List<friendsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriend(friend : friendsEntity)

    @Query("DELETE FROM friend_table")
    suspend fun clearFriends()

}