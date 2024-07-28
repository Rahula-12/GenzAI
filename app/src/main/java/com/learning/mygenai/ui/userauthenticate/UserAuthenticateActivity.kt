package com.learning.mygenai.ui.userauthenticate

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.learning.mygenai.R
import com.learning.mygenai.databinding.ActivityUserAuthenticateBinding
import com.learning.mygenai.ui.chatscreen.ChatActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject


private const val RC_SIGN_IN=9001
@AndroidEntryPoint
class UserAuthenticateActivity : AppCompatActivity() {

    @Inject
     lateinit var mGoogleSignInClient:GoogleSignInClient


     lateinit var viewModel: SignUpViewModel

    private lateinit var binding: ActivityUserAuthenticateBinding
    private lateinit var mAuth: FirebaseAuth
    private var verificationId:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser!=null) {
            val intent=Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_user_authenticate)
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.client_id))
//            .requestEmail()
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_authenticate)
        binding.viewmodel = viewModel
        binding.moveNext.setOnClickListener {
            if (binding.moveNext.text == "Sign Up") {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                if (email.isEmpty()) Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT)
                    .show()
                else if (password.isEmpty()) Toast.makeText(
                    this,
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Registered Successfully. Please login to proceed further.",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.email.setText("")
                            binding.password.setText("")
                            // viewModel.switchSighUp()
                        }
                }
            } else {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                if (email.isEmpty()) Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT)
                    .show()
                else if (password.isEmpty()) Toast.makeText(
                    this,
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                        Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                        binding.email.setText("")
                        binding.password.setText("")
                        val intent = Intent(this, ChatActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener(this) {
                        Toast.makeText(
                            this,
                            "Incorrect Email or Password. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
//            val intent= Intent(this,ChatActivity::class.java)
//            startActivity(intent)
        }

        binding.phoneButton.setOnClickListener{
            val phoneDialog= Dialog(this)
            phoneDialog.setContentView(R.layout.phone_number_input)
           // phoneNumberInputBinding=DataBindingUtil.inflate(layoutInflater,R.layout.phone_number_input,null,true)
            phoneDialog.window?.setLayout(1000, LayoutParams.WRAP_CONTENT)
            phoneDialog.setCancelable(true)
            phoneDialog.setTitle("Phone number verification")
            phoneDialog.show()
            val verificationButton=phoneDialog.findViewById<Button>(R.id.verifyButton)
            val submitButton=phoneDialog.findViewById<Button>(R.id.submitButton)
            val resendButton=phoneDialog.findViewById<Button>(R.id.resendButton)
            val otpText=phoneDialog.findViewById<EditText>(R.id.otp)
            val phoneText=phoneDialog.findViewById<EditText>(R.id.phoneNumber)
            verificationButton.setOnClickListener {
                if(!phoneText.text.toString().matches(Regex("[^a-zA-Z0-9]*[0-9]{3}[^a-zA-Z0-9]*[0-9]{3}[^a-zA-Z0-9]*[0-9]{4}\$"))) Toast.makeText(this,"Please enter correct phone number",Toast.LENGTH_SHORT).show()
                else {
                    val phoneNumber="+91${phoneText.text}"
                    submitButton.visibility = View.VISIBLE
                    resendButton.visibility = View.VISIBLE
                    otpText.visibility = View.VISIBLE
                    sendVerificationCode(phoneNumber)
                }
            }
            submitButton.setOnClickListener {
                if(otpText.text.toString().isEmpty())   Toast.makeText(this,"Please enter Otp",Toast.LENGTH_SHORT).show()
                else verifyCode(otpText.text.toString())
            }
        }
        viewModel.signUp.observe(this) {
            if (!it) {
                binding.changeOption1.text = "New User?"
                binding.changeOption2.text = "Sign Up"
                binding.moveNext.text = "Sign In"
            } else {
                binding.changeOption1.text = "Already Registered?"
                binding.changeOption2.text = "Sign In"
                binding.moveNext.text = "Sign Up"
            }
        }
        binding.googleButton.setOnClickListener {
            mAuth.signOut()
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            Toast.makeText(this@UserAuthenticateActivity,"Please wait while we are checking",Toast.LENGTH_SHORT).show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account?.run {
                    firebaseAuthWithGoogle(idToken!!)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent=Intent(this,ChatActivity::class.java)
                    Toast.makeText(this, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationCode(number: String) {
        // this method is used for getting
        // OTP on user phone number.
        val options =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(15L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(verificationId, p1)
                        this@UserAuthenticateActivity.verificationId=verificationId
                        Toast.makeText(this@UserAuthenticateActivity,"Please enter OTP sent to your phone number",Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        //Log.d("onVerificationCompleted",p0.smsCode.toString())
                        Toast.makeText(this@UserAuthenticateActivity, "Verification Completed Successfully.",Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(this@UserAuthenticateActivity, "Please enter Valid OTP.",Toast.LENGTH_SHORT).show()
                      // Log.d("otperror", p0.message.toString())
                    }

                }) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    val i: Intent = Intent(
                        this,
                        ChatActivity::class.java
                    )
                    Toast.makeText(this@UserAuthenticateActivity,"Logged In Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(i)
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    Toast.makeText(
                        this,
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun verifyCode(code: String) {
        // below line is used for getting
        // credentials from our verification id and code.
        val credential = PhoneAuthProvider.getCredential(verificationId?:"", code)


        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential)
    }

}