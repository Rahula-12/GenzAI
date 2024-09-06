package com.learning.mygenai.model.chatresponsemodel

data class Candidate(
    val content: Content,
    val finishReason: String?=null,
    val index: Int?=null,
    val safetyRatings: List<SafetyRating>?=null
)