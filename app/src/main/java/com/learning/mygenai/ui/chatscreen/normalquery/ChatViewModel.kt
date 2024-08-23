package com.learning.mygenai.ui.chatscreen.normalquery

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.mygenai.database.ChatRepository
import com.learning.mygenai.model.Chat
import com.learning.mygenai.model.Contents
import com.learning.mygenai.model.Parts
import com.learning.mygenai.model.Query
import com.learning.mygenai.network.RequestRepository
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
    private val chatRepository: ChatRepository
): ViewModel() {

    val wholeChat:LiveData<List<Chat>> = chatRepository.getAllChats()

    private val _waiting:MutableLiveData<Boolean> = MutableLiveData(false)
    val waiting:LiveData<Boolean> = _waiting

    fun askQuery(question:String) {
        viewModelScope.launch {
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
                val query=Query(contents = Contents(parts = Parts(question)))
                   val queryResponse=async(Dispatchers.IO) {
//                       delay(11000)
                       Log.d("normal_query","inside askQuery")
                       requestRepository.askQuery(query)
                   }
                    launch {
                        Log.d("normal_query","waiting to cancel")
                        delay(10000)
                        queryResponse.cancel()
                    }
                    val lastChat=chatRepository.getLastChat()
                Log.d("normal_query","got latest chat")
                    try {
                        Log.d("normal_query","updating lastChat")
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
        viewModelScope.launch {
            chatRepository.deleteChats()
        }
    }

}