package com.learning.mygenai.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.learning.mygenai.model.photodbmodel.PhotoChat
import javax.inject.Inject

@Dao
interface PhotoQueryDao {

    @Insert
    suspend fun insertPhotoChat(photoChat: PhotoChat)

    @Query("Select * from photo_query")
    fun allPhotoChats():LiveData<List<PhotoChat>>

    @Query("Delete from photo_query")
    suspend fun deletePhotoChats()

    @Query("SELECT * FROM photo_query ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastPhotoChat(): PhotoChat

    @Update
    suspend fun updatePhotoChat(photoChat: PhotoChat)
}