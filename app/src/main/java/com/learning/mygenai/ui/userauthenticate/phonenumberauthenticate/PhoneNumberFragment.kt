package com.learning.mygenai.ui.userauthenticate.phonenumberauthenticate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.google.ai.client.generativeai.type.content
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.learning.mygenai.R
import com.learning.mygenai.databinding.FragmentPhoneNumberBinding
import com.learning.mygenai.ui.userauthenticate.UserAuthenticateActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PhoneNumberFragment : Fragment() {
    private lateinit var binding:FragmentPhoneNumberBinding
    private lateinit var mAuth:FirebaseAuth
     lateinit var verificationId:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mAuth=FirebaseAuth.getInstance()
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_phone_number,container,false)
        binding.submitButton.setOnClickListener{
                if(!binding.editTextPhone.text.toString().matches(Regex("[^a-zA-Z0-9]*[0-9]{3}[^a-zA-Z0-9]*[0-9]{3}[^a-zA-Z0-9]*[0-9]{4}\$"))) Toast.makeText(requireContext(),"Please enter correct phone number",Toast.LENGTH_SHORT).show()
            else {
                val bundle=Bundle()
                    bundle.putString("phone_number","+91${binding.editTextPhone.text}")
                    sendVerificationCode("+91${binding.editTextPhone.text}",bundle)
            }
        }
        return binding.root
    }
    private fun sendVerificationCode(number: String,bundle: Bundle) {
        // this method is used for getting
        // OTP on user phone number.
        val options =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number) // Phone number to verify
                .setTimeout(15L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(requireActivity()) // Activity (for callback binding)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(verificationId, p1)
                        this@PhoneNumberFragment.verificationId=verificationId
                        Toast.makeText(requireContext(),"Please enter OTP sent to your phone number with ${this@PhoneNumberFragment.verificationId}",Toast.LENGTH_SHORT).show()
                        bundle.putString("verificationId",verificationId)
                        Log.d("verificationId","$verificationId in PhoneNumberFragment")
                        findNavController().navigate(R.id.action_phoneNumberFragment_to_otpEnterFragment,bundle)
                    }

                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        //Log.d("onVerificationCompleted",p0.smsCode.toString())
                        Toast.makeText(requireContext(), "Verification Completed Successfully.",Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Toast.makeText(requireContext(), p0.message.toString(),Toast.LENGTH_SHORT).show()
//                         Log.d("otperror", p0.message.toString())
                    }

                }) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }




}