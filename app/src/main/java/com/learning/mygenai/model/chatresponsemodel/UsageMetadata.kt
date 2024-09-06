package com.learning.mygenai.model.chatresponsemodel

data class UsageMetadata(
    val candidatesTokenCount: Int,
    val promptTokenCount: Int,
    val totalTokenCount: Int
)