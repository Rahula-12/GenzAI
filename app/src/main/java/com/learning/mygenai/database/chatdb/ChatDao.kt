package com.learning.mygenai.database.chatdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.learning.mygenai.model.chatdbmodel.Chat

@Dao
interface ChatDao {

    @Insert
    suspend fun insertChat(chat: Chat)

    @Query("Delete from Chat")
    suspend fun deleteChats()

    @Query("Select * from Chat")
    fun getAllChats():LiveData<List<Chat>>

    @Query("SELECT * FROM Chat ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastChat(): Chat

    @Update
    suspend fun updateChat(chat: Chat)

}