package com.learning.mygenai.database

import com.learning.mygenai.model.Chat
import okhttp3.internal.cacheGet
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatDao: ChatDao) {

    suspend fun insertChat(chat: Chat)=chatDao.insertChat(chat)

    suspend fun deleteChats()=chatDao.deleteChats()

    fun getAllChats() = chatDao.getAllChats()

    suspend fun getLastChat() = chatDao.getLastChat()

    suspend fun updateChat(chat: Chat) = chatDao.updateChat(chat)

}