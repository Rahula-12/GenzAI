package com.learning.mygenai.ui.userauthenticate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.learning.mygenai.R
import com.learning.mygenai.databinding.FragmentMainAuthenticateBinding
import com.learning.mygenai.ui.chatscreen.ChatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
private const val RC_SIGN_IN=9001

@AndroidEntryPoint
class MainAuthenticateFragment : Fragment() {

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var viewModel: SignUpViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentMainAuthenticateBinding
    private var verificationId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main_authenticate, container, false)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.viewmodel = viewModel
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            val chatIntent = Intent(requireContext(), ChatActivity::class.java)
            chatIntent.putExtras(requireActivity().intent)
            startActivity(chatIntent)
            requireActivity().finish()
        }
        binding.moveNext.setOnClickListener {
            if (binding.moveNext.text == "Sign Up") {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                if (email.isEmpty()) Toast.makeText(
                    requireContext(),
                    "Please enter email",
                    Toast.LENGTH_SHORT
                )
                    .show()
                else if (password.isEmpty()) Toast.makeText(
                    requireContext(),
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
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
                if (email.isEmpty()) Toast.makeText(
                    requireContext(),
                    "Please enter email",
                    Toast.LENGTH_SHORT
                )
                    .show()
                else if (password.isEmpty()) Toast.makeText(
                    requireContext(),
                    "Please enter password",
                    Toast.LENGTH_SHORT
                ).show()
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Logged in Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.email.setText("")
                        binding.password.setText("")
                        val chatIntent = Intent(requireContext(), ChatActivity::class.java)
                        chatIntent.putExtras(requireActivity().intent)
                        startActivity(chatIntent)
                        requireActivity().finish()
                    }.addOnFailureListener(requireActivity()) {
                        Toast.makeText(
                            requireContext(),
                            "Incorrect Email or Password. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        binding.phoneButton.setOnClickListener{
//            binding.fragmentContainerView2.visibility=View.VISIBLE
            this.findNavController().navigate(R.id.action_mainAuthenticateFragment_to_phoneNumberFragment)
//            val phoneDialog= Dialog(this)
//            phoneDialog.setContentView(R.layout.phone_number_input)
//           // phoneNumberInputBinding=DataBindingUtil.inflate(layoutInflater,R.layout.phone_number_input,null,true)
//            phoneDialog.window?.setLayout(1000, LayoutParams.WRAP_CONTENT)
//            phoneDialog.setCancelable(true)
//            phoneDialog.setTitle("Phone number verification")
//            phoneDialog.show()
//            val verificationButton=phoneDialog.findViewById<Button>(R.id.verifyButton)
//            val submitButton=phoneDialog.findViewById<Button>(R.id.submitButton)
//            val resendButton=phoneDialog.findViewById<Button>(R.id.resendButton)
//            val otpText=phoneDialog.findViewById<EditText>(R.id.otp)
//            val phoneText=phoneDialog.findViewById<EditText>(R.id.phoneNumber)
//            verificationButton.setOnClickListener {
//                if(!phoneText.text.toString().matches(Regex("[^a-zA-Z0-9]*[0-9]{3}[^a-zA-Z0-9]*[0-9]{3}[^a-zA-Z0-9]*[0-9]{4}\$"))) Toast.makeText(this,"Please enter correct phone number",Toast.LENGTH_SHORT).show()
//                else {
//                    val phoneNumber="+91${phoneText.text}"
//                    submitButton.visibility = View.VISIBLE
//                    resendButton.visibility = View.VISIBLE
//                    otpText.visibility = View.VISIBLE
//                    sendVerificationCode(phoneNumber)
//                }
//            }
//            submitButton.setOnClickListener {
//                if(otpText.text.toString().isEmpty())   Toast.makeText(this,"Please enter Otp",Toast.LENGTH_SHORT).show()
//                else verifyCode(otpText.text.toString())
//            }
        }
        viewModel.signUp.observe(viewLifecycleOwner) {
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
            return binding.root
        }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    account?.run {
                        firebaseAuthWithGoogle(idToken!!)
                        Toast.makeText(
                            requireContext(),
                            "Please wait while we are checking",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                }
            }
        }

        private fun firebaseAuthWithGoogle(idToken: String) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            requireContext(),
                            "Logged in Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        val chatIntent = Intent(requireContext(), ChatActivity::class.java)
                        chatIntent.putExtras(requireActivity().intent)
                        startActivity(chatIntent)
                        requireActivity().finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            requireContext(),
                            "Authentication Failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }