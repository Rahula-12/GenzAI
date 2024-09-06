package com.learning.mygenai.repositories

import com.learning.mygenai.database.chatdb.ChatDao
import com.learning.mygenai.model.chatdbmodel.Chat
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatDao: ChatDao) {

    suspend fun insertChat(chat: Chat)=chatDao.insertChat(chat)

    suspend fun deleteChats()=chatDao.deleteChats()

    fun getAllChats() = chatDao.getAllChats()

    suspend fun getLastChat() = chatDao.getLastChat()

    suspend fun updateChat(chat: Chat) = chatDao.updateChat(chat)

}