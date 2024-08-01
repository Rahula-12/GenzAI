package com.learning.mygenai.ui.chatscreen.normalquery

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

                chatRepository.insertChat(Chat(chatMessage = question, chatType = 0))

            launch {

                    _waiting.value = true

                chatRepository.insertChat(Chat(chatMessage = "#", chatType = 1))
                val query=Query(contents = Contents(parts = Parts(question)))
                val queryResponse=requestRepository.askQuery(query)
                val lastChat=chatRepository.getLastChat()
                lastChat.chatMessage=queryResponse.candidates[0].content.parts[0].text
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