package com.learning.mygenai.ui.chatscreen.picturequery

import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.learning.mygenai.R
import com.learning.mygenai.databinding.FragmentPhotoQueryBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Use the [PhotoQueryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PhotoQueryFragment : Fragment() {

    private lateinit var binding:FragmentPhotoQueryBinding
    private lateinit var dialogImageView:ConstraintLayout
    private lateinit var dialog:Dialog
    private var currentImage:Bitmap?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_photo_query,  container,false)
        val adapter=PhotoChatAdapter(false)
        val viewModel=ViewModelProvider(requireActivity())[PhotoQueryViewModel::class.java]
        Log.d("PhotoViewModel","Inside PhotoQueryFragment:$viewModel")
        viewModel.loading.observe(viewLifecycleOwner) {
            adapter.loading=it
        }
        binding.photoQueries.adapter=adapter
        dialog=Dialog(requireContext())
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
        binding.addQuery.setOnClickListener {
            dialog.show()
        }
        viewModel.allQueries.observe(viewLifecycleOwner){
            adapter.submitList(it)
            if(it.isNotEmpty())
            binding.photoQueries.smoothScrollToPosition(it.size-1)
        }
        if(arguments!=null) {
            val imageUri= requireArguments().getParcelable<Uri>("imageUri")
            imageUri?.let {

                val currentPhoto=dialogImageView.findViewById<ImageView>(R.id.currentPhoto)
                Glide.with(currentPhoto).asBitmap().load(it).into(currentPhoto)
                currentImage=uriToBitmap(it)
                dialogImageView.rotation= 90F
                dialog.show()
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode==-1 && requestCode==1888) {
            currentImage=data!!.extras!!["data"] as Bitmap
            if(::dialogImageView.isInitialized) {
                val currentPhoto=dialogImageView.findViewById<ImageView>(R.id.currentPhoto)
                Glide.with(currentPhoto).asBitmap().load(currentImage).into(currentPhoto)
//                val layoutParams=currentPhoto.layoutParams
//                layoutParams.height=300
//                layoutParams.width=1000
//                currentPhoto.layoutParams=layoutParams
                dialogImageView.rotation= 90F
            }
        }
    }


    @Throws(IOException::class)
    private fun uriToBitmap(uri: Uri): Bitmap? {
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bitmap
        } else {
            throw IOException("Unable to open InputStream from Uri")
        }
    }

}
