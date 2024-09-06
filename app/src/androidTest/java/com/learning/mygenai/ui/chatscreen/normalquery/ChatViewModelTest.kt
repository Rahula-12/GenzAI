package com.learning.mygenai.ui.chatscreen.normalquery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.learning.mygenai.ApplicationDispatchers
import com.learning.mygenai.BASE_URL
import com.learning.mygenai.FileReader
import com.learning.mygenai.database.chatdb.ChatDao
import com.learning.mygenai.network.ChatAPIService
import com.learning.mygenai.repositories.ChatRepository
import com.learning.mygenai.repositories.RequestRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltAndroidTest
class ChatViewModelTest {

    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()

    @get:Rule
    val hiltAndroidRule=HiltAndroidRule(this)


      private lateinit var chatViewModel: ChatViewModel
      private lateinit var chatRepository: ChatRepository
      private lateinit var requestRepository: RequestRepository
      private lateinit var mockWebServer: MockWebServer

      @Inject
      lateinit var chatDao: ChatDao
//
//      @Inject
//      lateinit var dispatchers: TestDispatchers

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        chatRepository= ChatRepository(chatDao)
        mockWebServer= MockWebServer()
        val mockResponse=MockResponse()
        mockResponse.setResponseCode(200)
        mockResponse.setBody(FileReader.readFile("/response.json"))
        mockWebServer.enqueue(mockResponse)
        val chatAPIService=Retrofit.Builder().baseUrl(mockWebServer.url("/")).
        addConverterFactory(GsonConverterFactory.create()).
        build().create(ChatAPIService::class.java)
        requestRepository=RequestRepository(chatAPIService)
    }

    @Test
    fun test_ask_query() = runTest{
//        val scheduler=TestCoroutineScheduler()
        val standardTestDispatcher= StandardTestDispatcher(testScheduler)
        val currentDispatchers=ApplicationDispatchers(ioDispatcher = standardTestDispatcher, mainDispatcher = standardTestDispatcher, defaultDispatcher = standardTestDispatcher)
        chatViewModel= ChatViewModel(requestRepository,chatRepository,currentDispatchers)
        chatViewModel.askQuery("Hi")
        advanceUntilIdle()
//        mockWebServer.takeRequest()
        assertEquals("How are you",chatDao.getLastChat().chatMessage)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}