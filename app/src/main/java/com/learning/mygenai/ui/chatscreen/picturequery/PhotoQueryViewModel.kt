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
import com.learning.mygenai.repositories.PhotoChatRepository
import com.learning.mygenai.model.photodbmodel.PhotoChat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                val path: String = MediaStore.Images.Media.insertImage(
                    application.contentResolver,
                    image,
                    image.toString(),
                    null
                )
                insertPhotoQuery(PhotoChat(photoUri = Uri.parse(path).toString(), userQuery = prompt, response = "We are facing some issue. Please try again."))
            }
            try {
                coroutineScope {
                    _loading.value=true
                    delay(1000)
                    val job = launch(Dispatchers.IO) {
//                        delay(6000)
//                        Log.d("photo_query","Entered")
                        val latestChat=photoChatRepository.getLastPhotoChat()
//                        Log.d("cfhj","1")
                        latestChat.response=generativeModel.generateContent(inputContent).text.toString()
//                        Log.d("cfhj","2")
                        photoChatRepository.updatePhotoChat(latestChat)
//                        Log.d("cfhj","3")
                        withContext(Dispatchers.Main){
                        _loading.value=false
                            }
                    }
                    launch {
                        delay(10000)
                        withContext(Dispatchers.IO) {
                            val latestChat = photoChatRepository.getLastPhotoChat()
                            if(latestChat.response=="We are facing some issue. Please try again.")
                                latestChat.response = "Please try again."
                            photoChatRepository.updatePhotoChat(latestChat)
                        }
                        _loading.value=false
                        job.cancel()
//                        Log.d("photo_query","Cancelled ${loading.value} ${allQueries.value?.size}")
                    }
                }
            }
            catch (e:Exception) {
                val latestChat=photoChatRepository.getLastPhotoChat()
                latestChat.response=e.message.toString()
                withContext(Dispatchers.Main) {
                    _loading.value = false
                }
            }

//            val bytes = ByteArrayOutputStream()
//            image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)


        }
    }




}