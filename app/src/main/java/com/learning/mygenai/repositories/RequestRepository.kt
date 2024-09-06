package com.learning.mygenai.repositories

import com.learning.mygenai.model.chatresponsemodel.Query
import com.learning.mygenai.network.ChatAPIService
import javax.inject.Inject

class RequestRepository @Inject constructor(private val chatAPIService: ChatAPIService) {

    suspend fun askQuery(query: Query)=chatAPIService.askQuery(query)

}