package com.learning.mygenai.network

import com.learning.mygenai.model.Query
import com.learning.mygenai.model.QueryResponse
import retrofit2.http.POST

interface ChatAPIService {

    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyAFJlA5u5lxfACrCk4VR8MFKOsxfXYLlFU")
    suspend fun askQuery(query: Query):QueryResponse

}