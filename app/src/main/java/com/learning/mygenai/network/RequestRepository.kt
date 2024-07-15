package com.learning.mygenai.network

import com.learning.mygenai.model.Query
import javax.inject.Inject

class RequestRepository @Inject constructor(private val chatAPIService: ChatAPIService) {

    suspend fun askQuery(query: Query)=chatAPIService.askQuery(query)

}