package com.learning.mygenai.ui.chatscreen.normalquery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.mygenai.ApplicationDispatchers
import com.learning.mygenai.repositories.ChatRepository
import com.learning.mygenai.model.chatdbmodel.Chat
import com.learning.mygenai.model.chatresponsemodel.Contents
import com.learning.mygenai.model.chatresponsemodel.Parts
import com.learning.mygenai.model.chatresponsemodel.Query
import com.learning.mygenai.repositories.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val requestRepository: RequestRepository,
    private val chatRepository: ChatRepository,
    private val applicationDispatchers: ApplicationDispatchers
): ViewModel() {

    val wholeChat:LiveData<List<Chat>> = chatRepository.getAllChats()

    private val _waiting:MutableLiveData<Boolean> = MutableLiveData(false)
    val waiting:LiveData<Boolean> = _waiting

    fun askQuery(question:String) {
        println("Riya")
        viewModelScope.launch(applicationDispatchers.mainDispatcher) {
            println("Jindal")
            launch {
                try {
                    coroutineScope {
                        chatRepository.insertChat(Chat(chatMessage = question, chatType = 0))
                    }
                }
                catch (e:Exception) {
                    Log.d("abc",e.message.toString())
                }
            }
            launch {
                Log.d("normal_query","Entered")
                _waiting.value = true
                chatRepository.insertChat(Chat(chatMessage = "We are facing some issue. Please try again.", chatType = 1))
                val query= Query(contents = Contents(parts = Parts(question)))
                   val queryResponse=async(applicationDispatchers.ioDispatcher) {
//                       delay(11000)
                       Log.d("normal_query", "Inside async block")
                       requestRepository.askQuery(query)
                   }
                    launch {
                        Log.d("normal_query","waiting to cancel")
                        delay(10000)
                        queryResponse.cancel()
                    }
                    val lastChat=chatRepository.getLastChat()
                    delay(1000)
                Log.d("normal_query","got latest chat")
                    try {
                        Log.d("normal_query","updating lastChat")
                        Log.d("normal_query1",queryResponse.await().candidates[0].content.parts[0].text)
                        lastChat.chatMessage=queryResponse.await().candidates[0].content.parts[0].text
                    }
                    catch (e:Exception) {
                        lastChat.chatMessage=e.message.toString()
                    }
                Log.d("normal_query","updated lastChat")
                    chatRepository.updateChat(lastChat)
                _waiting.value = false
            }
            //Log.d("Size",wholeChat.)
        }
    }

    fun deleteChats() {
        viewModelScope.launch(applicationDispatchers.mainDispatcher) {
            chatRepository.deleteChats()
        }
    }

}