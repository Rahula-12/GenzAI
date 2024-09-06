package com.learning.mygenai.normalqueries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.learning.mygenai.ApplicationDispatchers
import com.learning.mygenai.getOrAwaitValue
import com.learning.mygenai.model.chatresponsemodel.Candidate
import com.learning.mygenai.model.chatresponsemodel.Content
import com.learning.mygenai.model.chatresponsemodel.Contents
import com.learning.mygenai.model.chatresponsemodel.Parts
import com.learning.mygenai.model.chatresponsemodel.Query
import com.learning.mygenai.model.chatresponsemodel.QueryResponse
import com.learning.mygenai.repositories.ChatRepository
import com.learning.mygenai.repositories.RequestRepository
import com.learning.mygenai.ui.chatscreen.normalquery.ChatViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@HiltAndroidTest
class ChatViewModelTest {

    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()

    @get:Rule
    val hiltAndroidRule=HiltAndroidRule(this)

    @Mock
    lateinit var requestRepository: RequestRepository

    @Inject
    lateinit var chatRepository: ChatRepository

    private lateinit var viewModel: ChatViewModel

    @Inject
    lateinit var dispatchers: ApplicationDispatchers

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        hiltAndroidRule.inject()
        viewModel= ChatViewModel(requestRepository,chatRepository,dispatchers)
    }

    @Test
    fun test_ask_Query() {
        runTest {
            Mockito.`when`(requestRepository.askQuery(Query(contents = Contents(parts = Parts("Hi")))))
                .thenReturn(
                    QueryResponse(
                        candidates = listOf(
                            Candidate(
                                content = Content(
                                    parts = listOf(
                                        Parts("How are you")
                                    )
                                )
                            )
                        )
                    )
                )
            viewModel.askQuery("Hi")
            (dispatchers as TestDispatcher).scheduler.advanceUntilIdle()
            assertEquals(2, viewModel.wholeChat.getOrAwaitValue())
        }
    }

}