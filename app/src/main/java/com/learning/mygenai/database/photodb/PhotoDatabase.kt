package com.learning.mygenai.database.photodb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learning.mygenai.model.photodbmodel.PhotoChat

@Database(entities = [PhotoChat::class], exportSchema = false, version = 1)
abstract class PhotoDatabase:RoomDatabase() {

    abstract fun getPhotoQueryDao(): PhotoQueryDao

}