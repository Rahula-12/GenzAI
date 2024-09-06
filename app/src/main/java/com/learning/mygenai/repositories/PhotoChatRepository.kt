package com.learning.mygenai.repositories

import com.learning.mygenai.database.photodb.PhotoQueryDao
import com.learning.mygenai.model.photodbmodel.PhotoChat
import javax.inject.Inject

class PhotoChatRepository @Inject constructor(private val photoQueryDao: PhotoQueryDao) {

    suspend fun insertPhotoChat(photoChat: PhotoChat) = photoQueryDao.insertPhotoChat(photoChat)

    fun allPhotoChats() = photoQueryDao.allPhotoChats()

    suspend fun deletePhotoChats() = photoQueryDao.deletePhotoChats()

    suspend fun getLastPhotoChat() = photoQueryDao.getLastPhotoChat()

    suspend fun updatePhotoChat(photoChat: PhotoChat) = photoQueryDao.updatePhotoChat(photoChat)

}