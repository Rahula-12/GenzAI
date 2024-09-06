package com.learning.mygenai.di

import android.content.Context
import androidx.room.Room
import com.learning.mygenai.database.chatdb.ChatDao
import com.learning.mygenai.database.chatdb.ChatDatabase
import com.learning.mygenai.database.photodb.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
@Module
class TestDBModule {

    @Singleton
    @Provides
    fun providesChatDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.inMemoryDatabaseBuilder(context, ChatDatabase::class.java).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun providesChatDao(chatDatabase: ChatDatabase) : ChatDao {
        return chatDatabase.getChatDao()
    }

    @Singleton
    @Provides
    fun providesPhotoChatDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.inMemoryDatabaseBuilder(context, PhotoDatabase::class.java).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun providesPhotoQueryDao(photoDatabase: PhotoDatabase) = photoDatabase.getPhotoQueryDao()

}