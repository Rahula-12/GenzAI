package com.learning.mygenai.ui.userauthenticate.phonenumberauthenticate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.learning.mygenai.R
import com.learning.mygenai.databinding.FragmentOtpEnterBinding
import com.learning.mygenai.ui.chatscreen.ChatActivity


class OtpEnterFragment : Fragment() {
    private lateinit var binding:FragmentOtpEnterBinding
    private var verificationId:String?=null
    private lateinit var mAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth=FirebaseAuth.getInstance()
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_otp_enter,container,false)
        binding.otp1.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length==1) {
                    binding.otp2.requestFocus()
                }
            }

        })
        binding.otp2.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length==1) {
                    binding.otp3.requestFocus()
                }
            }

        })
        binding.otp3.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length==1) {
                    binding.otp4.requestFocus()
                }
            }

        })
        binding.otp4.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length==1) {
                    binding.otp5.requestFocus()
                }
            }

        })
        binding.otp5.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(s?.length==1) {
                    binding.otp6.requestFocus()
                }
            }

        })
        if(requireArguments().containsKey("phone_number")) {
            binding.phoneNumber=requireArguments().getString("phone_number")
        }
        if(requireArguments().containsKey("verificationId")) {
            verificationId=requireArguments().getString("verificationId")
            Log.d("verificationId","$verificationId in OtpEnterFragment")
        }
        binding.submitButton.setOnClickListener{
            val code="${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}${binding.otp5.text}${binding.otp6.text}"
            Log.d("OTP",code)
            verifyCode(code)
        }
        return binding.root
    }

    private fun verifyCode(code: String) {
        // below line is used for getting
        // credentials from our verification id and code.
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId ?: "", code)
            // after getting credential we are
            // calling sign in method.
            signInWithCredential(credential)
        }
        catch (e:Exception) {
            Log.d("verificationId","$verificationId in OtpEnterFragment")
            return
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // if the code is correct and the task is successful
                    // we are sending our user to new activity.
                    Toast.makeText(requireContext(),"Logged In Successfully",Toast.LENGTH_SHORT).show()
                    val chatIntent= Intent(requireContext(), ChatActivity::class.java)
                    chatIntent.putExtras(requireActivity().intent)
                    startActivity(chatIntent)
                    requireActivity().finish()
                } else {
                    // if the code is not correct then we are
                    // displaying an error message to the user.
                    Toast.makeText(
                        requireContext(),
                        task.exception?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}