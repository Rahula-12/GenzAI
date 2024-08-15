package com.learning.mygenai

import android.content.Context
import androidx.appcompat.app.AlertDialog

const val BASE_URL="https://generativelanguage.googleapis.com"

fun internetDialog(context: Context): AlertDialog {
    val internetAlertDialog= AlertDialog.Builder(context).setTitle("Internet Connectivity Alert").setCancelable(true).setMessage("Please check your internet connection.").setPositiveButton("OK"){
            _,_->
    }
    // phoneNumberInputBinding=DataBindingUtil.inflate(layoutInflater,R.layout.phone_number_input,null,true)
    internetAlertDialog.setCancelable(true)
    return internetAlertDialog.show()
}