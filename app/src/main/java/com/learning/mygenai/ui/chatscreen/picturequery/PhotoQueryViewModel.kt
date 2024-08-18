package com.learning.mygenai.ui.chatscreen.picturequery

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.learning.mygenai.database.PhotoChatRepository
import com.learning.mygenai.model.PhotoChat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class PhotoQueryViewModel @Inject constructor(private val application: Application, private val photoChatRepository: PhotoChatRepository):AndroidViewModel(application) {

     val allQueries: LiveData<List<PhotoChat>> = photoChatRepository.allPhotoChats()
    private val _loading:MutableLiveData<Boolean> = MutableLiveData(false)
    val loading:LiveData<Boolean> = _loading
    private fun insertPhotoQuery(photoChat: PhotoChat) {
        viewModelScope.launch {
            photoChatRepository.insertPhotoChat(photoChat)
            photoChatRepository
        }
    }

    fun deleteAllChats() {
        viewModelScope.launch {
            photoChatRepository.deletePhotoChats()
        }
    }

    fun getResponse(image:Bitmap, prompt:String) {
        viewModelScope.launch {
            val generativeModel = GenerativeModel(
                // The Gemini 1.5 models are versatile and work with both text-only and multimodal prompts
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyAFJlA5u5lxfACrCk4VR8MFKOsxfXYLlFU"
            )
//            val bitmap = application.contentResolver.openInputStream(image).use { data ->
//                BitmapFactory.decodeStream(data)
//            }
            val inputContent = content {
                image(image)
                text(prompt)
            }
            launch {
                _loading.value=true
                val path: String = MediaStore.Images.Media.insertImage(
                    application.contentResolver,
                    image,
                    image.toString(),
                    null
                )
                insertPhotoQuery(PhotoChat(photoUri = Uri.parse(path).toString(), userQuery = prompt, response = "#"))
            }
            launch {
                val response = generativeModel.generateContent(inputContent).text.toString()
                val latestChat=photoChatRepository.getLastPhotoChat()
                latestChat.response=response
                photoChatRepository.updatePhotoChat(latestChat)
                _loading.value=false
            }

//            val bytes = ByteArrayOutputStream()
//            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)


        }
    }




}