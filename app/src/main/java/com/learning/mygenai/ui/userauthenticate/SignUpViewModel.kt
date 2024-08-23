package com.learning.mygenai.ui.userauthenticate

import android.util.Log
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {

    private val _signUp=MutableLiveData(true)
    val signUp:LiveData<Boolean> = _signUp

    fun switchSighUp() {
        _signUp.value=!(_signUp.value)!!
    }

    fun testingCoroutine() {
        Log.d("CoroutineTest","Beginning")
        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main) {
                delay(1000)
                Log.d("CoroutineTest","inside Coroutine1")
            }
//            launch {
                Log.d("CoroutineTest", "inside Coroutine2")
//            }
        }
        Log.d("CoroutineTest","Ending")
    }


}