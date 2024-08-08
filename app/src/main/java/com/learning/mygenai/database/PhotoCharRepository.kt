package com.learning.mygenai.database

import com.learning.mygenai.model.PhotoChat
import javax.inject.Inject

class PhotoChatRepository @Inject constructor(private val photoQueryDao: PhotoQueryDao) {

    suspend fun insertPhotoChat(photoChat: PhotoChat) = photoQueryDao.insertPhotoChat(photoChat)

    fun allPhotoChats() = photoQueryDao.allPhotoChats()

    suspend fun deletePhotoChats() = photoQueryDao.deletePhotoChats()

    suspend fun getLastPhotoChat() = photoQueryDao.getLastPhotoChat()

    suspend fun updatePhotoChat(photoChat: PhotoChat) = photoQueryDao.updatePhotoChat(photoChat)

}