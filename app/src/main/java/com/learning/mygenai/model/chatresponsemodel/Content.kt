package com.learning.mygenai.model.chatresponsemodel

data class Content(
    val parts: List<Parts>,
    val role: String?=null
)