package com.learning.mygenai.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learning.mygenai.model.PhotoChat

@Database(entities = [PhotoChat::class], exportSchema = false, version = 1)
abstract class PhotoDatabase:RoomDatabase() {

    abstract fun getPhotoQueryDao():PhotoQueryDao

}