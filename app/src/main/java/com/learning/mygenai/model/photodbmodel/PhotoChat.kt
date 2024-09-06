package com.learning.mygenai.model.photodbmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.util.UUID

@Entity(tableName = "photo_query")
data class PhotoChat (
    @PrimaryKey
    val uuid: String=UUID.randomUUID().toString(),
    val photoUri:String,
    val userQuery:String,
    var response:String,
    val timestamp: String=Timestamp.now().toString()
)