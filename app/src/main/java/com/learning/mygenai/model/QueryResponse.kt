package com.learning.mygenai.model

data class QueryResponse(
    val candidates: List<Candidate>,
    val usageMetadata: UsageMetadata
)