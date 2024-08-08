package com.learning.mygenai.ui.chatscreen.picturequery

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.learning.mygenai.R
import com.learning.mygenai.databinding.FragmentPhotoQueryBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoQueryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PhotoQueryFragment : Fragment() {

    private lateinit var binding:FragmentPhotoQueryBinding
    private lateinit var dialogImageView:ConstraintLayout
    private var currentImage:Bitmap?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_photo_query,  container,false)
        val adapter=PhotoChatAdapter(false)
        val viewModel=ViewModelProvider(requireActivity())[PhotoQueryViewModel::class.java]
        viewModel.loading.observe(viewLifecycleOwner) {
            adapter.loading=it
        }
        binding.photoQueries.adapter=adapter
        binding.addQuery.setOnClickListener {
            val dialog=Dialog(requireContext())
            dialog.setTitle("Photo Query")
            dialog.setContentView(R.layout.photo_query_dialog)
            dialogImageView=dialog.findViewById(R.id.image_upload)
            dialogImageView.setOnClickListener{
                val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
                startActivityForResult(intent,1888)
            }
            val submitButton=dialog.findViewById<Button>(R.id.askButton)
            val prompt=dialog.findViewById<TextView>(R.id.prompt)
            submitButton.setOnClickListener {
                if(currentImage==null) {
                    Toast.makeText(requireContext(),"Please upload image first",Toast.LENGTH_SHORT).show()
                }
                else if(prompt.text=="") {
                    Toast.makeText(requireContext(),"Please enter prompt.",Toast.LENGTH_SHORT).show()
                }
                else {
                    viewModel.getResponse(currentImage!!,prompt.text.toString())
                    currentImage=null
                    prompt.text = ""
                    dialog.cancel()
                }
            }
            dialog.setOnCancelListener{
                currentImage=null
            }
            dialog.show()
        }
        viewModel.allQueries.observe(viewLifecycleOwner){
            adapter.submitList(it)
            if(it.isNotEmpty())
            binding.photoQueries.smoothScrollToPosition(it.size-1)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode==-1 && requestCode==1888) {
            currentImage=data!!.extras!!["data"] as Bitmap
            if(::dialogImageView.isInitialized) {
                val currentPhoto=dialogImageView.findViewById<ImageView>(R.id.currentPhoto)
                Glide.with(currentPhoto).asBitmap().load(currentImage).into(currentPhoto)
                dialogImageView.rotation= 90F
            }
        }
    }

}