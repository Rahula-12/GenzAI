package com.learning.mygenai.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learning.mygenai.model.Chat

@Database(entities = [Chat::class], version = 2, exportSchema = false)
abstract class ChatDatabase():RoomDatabase() {
   abstract fun getChatDao(): ChatDao
}