package com.learning.mygenai.ui.chatscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.learning.mygenai.R
import com.learning.mygenai.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val viewModel: ChatViewModel by activityViewModels<ChatViewModel>()
    private lateinit var binding:FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_chat,container,false)
      //  viewModel= ViewModelProvider(this)[ChatViewModel::class.java]
        binding.viewmodel=viewModel
        val adapter=ChatAdapter()
        binding.chat.adapter=adapter
        binding.askButton.setOnClickListener {
            if(binding.prompt.text.toString()=="")  Toast.makeText(activity,"Please ask some question",Toast.LENGTH_SHORT).show()
            else {
                viewModel.askQuery(binding.prompt.text.toString())
                binding.prompt.setText("")
            }
        }
        viewModel.wholeChat.observe(viewLifecycleOwner) { it ->
            adapter.data=it
            Log.d("Size",adapter.data.size.toString())
        }
        return binding.root
    }
}