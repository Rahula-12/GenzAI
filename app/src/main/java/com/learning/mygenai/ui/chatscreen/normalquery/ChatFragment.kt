package com.learning.mygenai.ui.chatscreen.normalquery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        binding=FragmentChatBinding.inflate(inflater,container,false)
        //  viewModel= ViewModelProvider(this)[ChatViewModel::class.java]
        val adapter= ChatAdapter(false)
        binding.chat.adapter = adapter
        viewModel.waiting.observe(viewLifecycleOwner) {
           adapter.waiting = it
        }
        viewModel.wholeChat.observe(viewLifecycleOwner) { chatList ->
            adapter.submitList(chatList)
            if(chatList.isNotEmpty())
            binding.chat.smoothScrollToPosition(chatList.size-1)
            // Log.d("Size",adapter.data.size.toString())
        }
        binding.askButton.setOnClickListener {
            if (binding.prompt.text.toString() == "") Toast.makeText(
                activity,
                "Please ask some question",
                Toast.LENGTH_SHORT
            ).show()
            else {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
                viewModel.askQuery(binding.prompt.text.toString())
                binding.prompt.setText("")
            }
        }
        return binding.root
    }
}