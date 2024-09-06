package com.learning.mygenai.repositories

import FileReader
import com.learning.mygenai.model.chatresponsemodel.Candidate
import com.learning.mygenai.model.chatresponsemodel.Content
import com.learning.mygenai.model.chatresponsemodel.Contents
import com.learning.mygenai.model.chatresponsemodel.Parts
import com.learning.mygenai.model.chatresponsemodel.Query
import com.learning.mygenai.model.chatresponsemodel.QueryResponse
import com.learning.mygenai.network.ChatAPIService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RequestRepositoryTest {

    lateinit var mockWebServer: MockWebServer

    private lateinit var repository: RequestRepository

    @Before
    fun setUp() {
        mockWebServer= MockWebServer()
        val chatAPIService= Retrofit.Builder().baseUrl(mockWebServer.url("/"))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(ChatAPIService::class.java)
        repository= RequestRepository(chatAPIService)
    }

    @Test
    fun test_api_response_empty() = runTest{
        val mockResponse=MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody("{\n" +
                "    \"candidates\": [\n" +
                "       \n" +
                "    ]\n" +
                "}")
        mockWebServer.enqueue(mockResponse)
        val queryResponse=repository.askQuery(Query(contents = Contents(parts = Parts("Hi"))))
        mockWebServer.takeRequest()
        assertEquals(0,queryResponse.candidates.size)
//        queryResponse=repository.askQuery(Query(contents = Contents(parts = Parts("How are you sir"))))
//        assertEquals("Hi Sir! How are you",queryResponse.candidates[0].content.parts[0].text)
    }

    @Test
    fun test_api_response_message() = runTest{
        val mockResponse=MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(FileReader.readFile("/response.json"))
        mockWebServer.enqueue(mockResponse)
        val queryResponse=repository.askQuery(Query(contents = Contents(parts = Parts("Hi"))))
        mockWebServer.takeRequest()
        assertEquals("How are you",queryResponse.candidates[0].content.parts[0].text)
//        queryResponse=repository.askQuery(Query(contents = Contents(parts = Parts("How are you sir"))))
//        assertEquals("Hi Sir! How are you",queryResponse.candidates[0].content.parts[0].text)
    }

    @Test(expected = Exception::class)
    fun test_api_response_error() = runTest{
        val mockResponse=MockResponse()
        mockResponse.setResponseCode(400)
        mockResponse.setBody("Bad Request")
        mockWebServer.enqueue(mockResponse)
        repository.askQuery(Query(contents = Contents(parts = Parts("Hi"))))
        mockWebServer.takeRequest()
//        queryResponse=repository.askQuery(Query(contents = Contents(parts = Parts("How are you sir"))))
//        assertEquals("Hi Sir! How are you",queryResponse.candidates[0].content.parts[0].text)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}