package com.learning.mygenai.ui.userauthenticate

import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {

    private val _signUp=MutableLiveData(true)
    val signUp:LiveData<Boolean> = _signUp

    fun switchSighUp() {
        _signUp.value=!(_signUp.value)!!
    }


}