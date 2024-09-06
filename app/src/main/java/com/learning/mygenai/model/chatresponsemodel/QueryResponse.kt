package com.learning.mygenai.model.chatresponsemodel

data class QueryResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata?=null
)