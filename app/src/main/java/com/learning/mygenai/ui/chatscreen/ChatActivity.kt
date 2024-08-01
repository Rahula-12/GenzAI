package com.learning.mygenai.ui.chatscreen

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.learning.mygenai.R
import com.learning.mygenai.databinding.*
import com.learning.mygenai.ui.chatscreen.normalquery.ChatViewModel
import com.learning.mygenai.ui.userauthenticate.UserAuthenticateActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    @Inject
     lateinit var googleSignInClient: GoogleSignInClient

     lateinit var viewModel: ChatViewModel
//    private lateinit var button: Button
    private lateinit var binding: ActivityMainBinding
    //private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Log.d("currentUser",FirebaseAuth.getInstance().currentUser?.email.toString())
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
//        binding.myToolbar.showOverflowMenu()
        setSupportActionBar(binding.myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        binding.myToolbar.clearFocus()
        viewModel= ViewModelProvider(this)[ChatViewModel::class.java]
        binding.clearButton.setOnClickListener{
            viewModel.deleteChats()
        }

        binding.logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
//            val googleSignInClient=GoogleSignIn.getClient(this,
//                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
            googleSignInClient.signOut()
            val intent=Intent(this,UserAuthenticateActivity::class.java)
            Toast.makeText(this,"Logged Out Successfully",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
        enableEdgeToEdge()
//        setSupportActionBar(findViewById(R.id.my_toolbar))
        setContentView(binding.root)
      //  binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
      //  setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.clear && ::viewModel.isInitialized) {
            viewModel.deleteChats()
        }
        return true
    }

}