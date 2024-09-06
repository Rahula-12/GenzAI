package com.learning.mygenai.di

import android.content.Context
import androidx.room.Room
import com.learning.mygenai.database.chatdb.ChatDao
import com.learning.mygenai.database.chatdb.ChatDatabase
import com.learning.mygenai.database.photodb.PhotoDatabase
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
    fun providesChatDatabase(@ApplicationContext context:Context): ChatDatabase {
        return Room.databaseBuilder(context, ChatDatabase::class.java,"chat_db").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesChatDao(chatDatabase: ChatDatabase) : ChatDao {
        return chatDatabase.getChatDao()
    }

    @Singleton
    @Provides
    fun providesPhotoChatDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.databaseBuilder(context, PhotoDatabase::class.java,"photo_query_db").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesPhotoQueryDao(photoDatabase: PhotoDatabase) = photoDatabase.getPhotoQueryDao()

}