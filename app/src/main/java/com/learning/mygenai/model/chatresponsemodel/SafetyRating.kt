package com.learning.mygenai.model.chatresponsemodel

data class SafetyRating(
    val category: String,
    val probability: String
)