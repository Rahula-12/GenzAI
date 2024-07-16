package com.learning.mygenai.di

import android.content.Context
import androidx.room.Room
import com.learning.mygenai.database.ChatDao
import com.learning.mygenai.database.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesChatDatabase(@ApplicationContext context:Context):ChatDatabase {
        return Room.databaseBuilder(context,ChatDatabase::class.java,"chat_db").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesChatDao(chatDatabase: ChatDatabase) :ChatDao {
        return chatDatabase.getChatDao()
    }

}