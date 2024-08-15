package com.learning.mygenai.internetAlertWorker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.learning.mygenai.GenAIApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class InternetAlertWorker(private val appContext: Context,workParams:WorkerParameters):CoroutineWorker(appContext,workParams) {
    override suspend fun doWork(): Result {
        val connectivity=isNetworkAvailable(appContext)
        if(!connectivity) {
            val application=applicationContext as GenAIApplication
            withContext(Dispatchers.Main){
                application.changeAlert()
            }
        }
        delay(60000L)
        val workRequest= OneTimeWorkRequestBuilder<InternetAlertWorker>().build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork("Internet Alert",
            ExistingWorkPolicy.REPLACE,workRequest)
        return Result.success()
    }
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}