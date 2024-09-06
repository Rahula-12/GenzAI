package com.learning.mygenai.model.chatdbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.UUID

@Entity
data class Chat(
    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    var chatMessage:String,
    val chatType:Int,
    val timestamp: String=Timestamp.now().toString()
)