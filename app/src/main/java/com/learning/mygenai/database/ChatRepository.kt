package com.learning.mygenai.database

import com.learning.mygenai.model.Chat
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatDao: ChatDao) {

    suspend fun insertChat(chat: Chat)=chatDao.insertChat(chat)

    suspend fun deleteChats()=chatDao.deleteChats()

     fun getAllChats() = chatDao.getAllChats()

}