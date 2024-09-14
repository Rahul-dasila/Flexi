package com.example.flexie.di

import android.content.Context
import androidx.room.Room
import com.example.flexie.dao.conversationDao
import com.example.flexie.dao.friendsDao
import com.example.flexie.daoDatabases.conversationDatabase
import com.example.flexie.daoDatabases.friendDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {
    @Provides
    @Singleton
    fun provideFriendDatabase(@ApplicationContext context: Context): friendDatabase {
        return Room.databaseBuilder(context, friendDatabase::class.java, "friendDB").build()
    }

    @Provides
    @Singleton
    fun provideFriendDao(database: friendDatabase) : friendsDao{
        return database.friendDao()
    }

    @Provides
    @Singleton
    fun provideConversationDatabase(@ApplicationContext context: Context) : conversationDatabase{
        return Room.databaseBuilder(context,conversationDatabase::class.java , "conversationDB").build()
    }

    @Provides
    @Singleton
    fun provideConversationDao(db :conversationDatabase) : conversationDao{
        return db.conversationDao()
    }
}