package com.learning.mygenai.ui.userauthenticate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.learning.mygenai.R
import com.learning.mygenai.internetDialog
import com.learning.mygenai.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UserAuthenticateActivity : AppCompatActivity() {

    private lateinit var receiver:BroadcastReceiver

    private lateinit var networkCallback: NetworkCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_authenticate)
    }

    override fun onStart() {
        super.onStart()
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    context?.let {
                        if (!isNetworkAvailable(it)) {
                            lifecycleScope.launch {
                                while (!isNetworkAvailable(it)) {
                                    internetDialog(this@UserAuthenticateActivity)
                                    delay(10000L)
                                }
                            }
                        }
                    }

                }
            }
            registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        else {
             networkCallback=object: NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    lifecycleScope.launch {
                        while (!isNetworkAvailable(this@UserAuthenticateActivity)) {
                            internetDialog(this@UserAuthenticateActivity)
                            delay(10000L)
                        }
                    }
                }
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    if (!hasInternet) {
                        // Internet is not working
                        lifecycleScope.launch {
                            while (!isNetworkAvailable(this@UserAuthenticateActivity)) {
                                internetDialog(this@UserAuthenticateActivity)
                                delay(10000L)
                            }
                        }
                    }
                }
            }
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    override fun onStop() {
        super.onStop()
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.N) {
            if (::receiver.isInitialized) unregisterReceiver(receiver)
        }
        else {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if(::networkCallback.isInitialized) connectivityManager.unregisterNetworkCallback(networkCallback)
        }

    }

}