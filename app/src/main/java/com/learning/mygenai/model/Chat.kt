package com.learning.mygenai.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Chat(
    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    val chatMessage:String,
    val chatType:Int
)