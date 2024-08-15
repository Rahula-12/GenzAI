package com.learning.mygenai

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GenAIApplication:Application() {
    private val _internetAlert:MutableLiveData<Boolean> = MutableLiveData(false)
    val internetAlert:LiveData<Boolean> = _internetAlert

    fun changeAlert() {
        _internetAlert.value=!_internetAlert.value!!
    }
}