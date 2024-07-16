package com.learning.mygenai.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.learning.mygenai.model.Chat

@Dao
interface ChatDao {

    @Insert
    suspend fun insertChat(chat: Chat)

    @Query("Delete from Chat")
    suspend fun deleteChats()

    @Query("Select * from Chat")
    fun getAllChats():LiveData<Chat>

}