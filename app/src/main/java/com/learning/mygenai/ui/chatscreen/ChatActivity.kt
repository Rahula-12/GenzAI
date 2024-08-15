package com.learning.mygenai.ui.chatscreen

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.learning.mygenai.GenAIApplication
import com.learning.mygenai.R
import com.learning.mygenai.databinding.*
import com.learning.mygenai.internetAlertWorker.InternetAlertWorker
import com.learning.mygenai.internetDialog
import com.learning.mygenai.ui.chatscreen.normalquery.ChatFragment
import com.learning.mygenai.ui.chatscreen.normalquery.ChatViewModel
import com.learning.mygenai.ui.chatscreen.picturequery.PhotoQueryFragment
import com.learning.mygenai.ui.chatscreen.picturequery.PhotoQueryViewModel
import com.learning.mygenai.ui.userauthenticate.UserAuthenticateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    @Inject
     lateinit var googleSignInClient: GoogleSignInClient
    lateinit var currentMenu:Menu
     lateinit var chatViewModel: ChatViewModel
     lateinit var photoQueryViewModel: PhotoQueryViewModel
//    private lateinit var button: Button
    private lateinit var binding: ActivityMainBinding
    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("currentUser",FirebaseAuth.getInstance().currentUser?.email.toString())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding.myToolbar.showOverflowMenu()
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        binding.myToolbar.clearFocus()
        if (FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(this, "Please Sign in first", Toast.LENGTH_SHORT).show()
            finish()
            val authenticateIntent = Intent(this, UserAuthenticateActivity::class.java)
            authenticateIntent.putExtras(intent)
            startActivity(authenticateIntent)
        }
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        Log.d("ViewModel", "Inside ChatActivity:$chatViewModel")
        photoQueryViewModel = ViewModelProvider(this)[PhotoQueryViewModel::class.java]
        Log.d("PhotoViewModel", "Inside ChatActivity:$photoQueryViewModel")
        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
//            val googleSignInClient=GoogleSignIn.getClient(this,
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            googleSignInClient.signOut()
            val intent = Intent(this, UserAuthenticateActivity::class.java)
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        enableEdgeToEdge()
//        setSupportActionBar(findViewById(R.id.my_toolbar))
        setContentView(binding.root)
        Log.d("IntentType", intent.type.toString())
        if (intent.type?.contains("image") == true) {
            Log.d("Inside", "Inside")
            val navHostFragment: Fragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val bundle=Bundle()
            bundle.putParcelable("imageUri",intent.getParcelableExtra(Intent.EXTRA_STREAM))
            Log.d("imageUri", intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM).toString())
            navHostFragment.findNavController()
                .navigate(R.id.action_chatFragment_to_photoQueryFragment,bundle)
        }
        lifecycleScope.launch {
            while (true){
                val connectivity=isNetworkAvailable(this@ChatActivity)
                if(!connectivity) {
                    internetDialog(this@ChatActivity)
                }
                delay(10000L)
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    override fun onStart() {
        super.onStart()
        val workRequest= OneTimeWorkRequestBuilder<InternetAlertWorker>().build()
        WorkManager.getInstance(this).enqueueUniqueWork("Internet Alert",
            ExistingWorkPolicy.REPLACE,workRequest)
    }

    override fun onStop() {
        super.onStop()
        WorkManager.getInstance(this).cancelUniqueWork("Internet Alert")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        currentMenu=menu
        val normalQuery=menu.findItem(R.id.normal_query)
        val photoQuery=menu.findItem(R.id.photo_query)
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        val topFragment = navHostFragment!!.childFragmentManager.fragments.lastOrNull()
        Log.d("Clear",topFragment?.javaClass.toString())
        if(topFragment is PhotoQueryFragment) {
            photoQuery.setVisible(false)
//            onCreateOptionsMenu(menu)
        }
        else {
            normalQuery.setVisible(false)
//            onCreateOptionsMenu(menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.clear) {
            val navHostFragment: Fragment? =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            val topFragment = navHostFragment!!.childFragmentManager.fragments.lastOrNull()
//            Log.d("Clear",navController.currentDestination?.id.toString()+" "+R.layout.fragment_photo_query+" "+R.layout.fragment_chat)
            if(topFragment is PhotoQueryFragment) {
                photoQueryViewModel.deleteAllChats()
            }
            else {
                chatViewModel.deleteChats()
            }
        }
        else if(item.itemId==R.id.photo_query) {
            val navController=findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.action_chatFragment_to_photoQueryFragment)
            if(::currentMenu.isInitialized) {
//                currentMenu.clear()
                    lifecycleScope.launch {
                        delay(1000)
                        item.setVisible(false)
                        currentMenu.getItem(1).setVisible(true)
                    }

            }
        }
        else if(item.itemId==R.id.normal_query) {
            val navController=findNavController(R.id.fragmentContainerView)
            navController.navigate(R.id.action_photoQueryFragment_to_chatFragment)
            if(::currentMenu.isInitialized) {
//                currentMenu.clear()
                lifecycleScope.launch {
                    delay(1000)
                    currentMenu.getItem(0).setVisible(true)
                    item.setVisible(false)
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}