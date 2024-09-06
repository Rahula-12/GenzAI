package com.learning.mygenai.tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.learning.mygenai.database.chatdb.ChatDao
import com.learning.mygenai.database.photodb.PhotoQueryDao
import com.learning.mygenai.getOrAwaitValue
import com.learning.mygenai.model.chatdbmodel.Chat
import com.learning.mygenai.model.photodbmodel.PhotoChat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class AppDBTests {

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule= InstantTaskExecutorRule()

//    private val testDispatcher=StandardTestDispatcher()

    @Inject
    lateinit var chatDao: ChatDao

    @Inject
    lateinit var photoQueryDao: PhotoQueryDao

    @Before
    fun setUp() {
        //Arrange
        hiltAndroidRule.inject()
    }

    @Test
    fun insert_chat_query_test() = runBlocking {
        //Act
        val currChat= Chat(chatMessage = "How are you", chatType = 0)
        chatDao.insertChat(currChat)
        val chats = chatDao.getAllChats().getOrAwaitValue()
        assertEquals(currChat,chats[0])
        assertEquals(1, chats.size)
    }

    @Test
    fun delete_chat_queries_test() = runBlocking {
        //Act
        chatDao.insertChat(Chat(chatMessage = "How are you", chatType = 0))
        val chats = chatDao.getAllChats().getOrAwaitValue()
        assertEquals(1, chats.size)
        chatDao.deleteChats()
        assertEquals(0,chatDao.getAllChats().getOrAwaitValue().size)
    }

    @Test
    fun get_latest_chat_query_test() = runBlocking {
        chatDao.insertChat(Chat(chatMessage = "How are you", chatType = 0))
        val response= Chat(chatMessage = "Fine sir. How r u?", chatType = 1)
        chatDao.insertChat(response)
        val latestChat=chatDao.getLastChat()
        assertEquals(true,response==latestChat)
    }

    @Test
    fun update_chat_query_test() = runBlocking {
        val chat= Chat(chatMessage = "How are you", chatType = 0)
        chatDao.insertChat(chat)
        chat.chatMessage="What do you do"
        chatDao.updateChat(chat)
        assertEquals(chat,chatDao.getLastChat())
    }

    @Test
    fun insert_photo_query_test() = runBlocking {
        //Act
        val currPhotoChat=
            PhotoChat(photoUri = "riyajindal@love.com", userQuery = "Who is she?", response = "She is your love sir")
        photoQueryDao.insertPhotoChat(currPhotoChat)
        val chats = photoQueryDao.allPhotoChats().getOrAwaitValue()
        assertEquals(currPhotoChat,chats[0])
        assertEquals(1, chats.size)
    }

    @Test
    fun delete_photoChat_queries_test() = runBlocking {
        //Act
        photoQueryDao.insertPhotoChat(PhotoChat(photoUri = "riyajindal@love.com", userQuery = "Who is she?", response = "She is your love sir"))
        val chats = photoQueryDao.allPhotoChats().getOrAwaitValue()
        assertEquals(1, chats.size)
        photoQueryDao.deletePhotoChats()
        assertEquals(0,photoQueryDao.allPhotoChats().getOrAwaitValue().size)
    }

    @Test
    fun update_photoChat_query_test() = runBlocking {
        val photoChat=
            PhotoChat(photoUri = "riyajindal@love.com", userQuery = "Who is she?", response = "She is your love sir and she likes you")
        photoQueryDao.insertPhotoChat(photoChat)
        photoChat.response="${photoChat.response} And she wants to meet you sir."
        photoQueryDao.updatePhotoChat(photoChat)
        assertEquals(photoChat,photoQueryDao.getLastPhotoChat())
    }

    @Test
    fun get_latest_photoChat_query_test() = runBlocking {
        val photoChat=
            PhotoChat(photoUri = "riyajindal@love.com", userQuery = "Who is she?", response = "She is your love sir and she likes you")
        photoQueryDao.insertPhotoChat(photoChat)
        val latestPhotoChat=photoQueryDao.getLastPhotoChat()
        assertEquals(true,photoChat==latestPhotoChat)
    }

}