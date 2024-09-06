package com.learning.mygenai.database.chatdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learning.mygenai.model.chatdbmodel.Chat

@Database(entities = [Chat::class], version = 2, exportSchema = false)
abstract class ChatDatabase():RoomDatabase() {
   abstract fun getChatDao(): ChatDao
}