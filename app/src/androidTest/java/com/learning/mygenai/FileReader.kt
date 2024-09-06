package com.learning.mygenai

import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import java.io.InputStreamReader

object FileReader {

    fun readFile(file:String):String {
        val inputStream=Helper::class.java.getResourceAsStream(file)
        val builder=StringBuilder()
        val inputStreamReader=InputStreamReader(inputStream,"UTF-8")
        inputStreamReader.readLines().forEach{
            builder.append(it+"\n")
        }
        return builder.toString()
    }

}