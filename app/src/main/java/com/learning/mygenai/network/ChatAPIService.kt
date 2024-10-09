package com.learning.mygenai.network

import com.learning.mygenai.BuildConfig
import com.learning.mygenai.model.chatresponsemodel.Query
import com.learning.mygenai.model.chatresponsemodel.QueryResponse
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface ChatAPIService {

    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent?key=${com.learning.mygenai.BuildConfig.API_KEY}")
    suspend fun askQuery(@Body query: Query): QueryResponse

}