package com.learning.mygenai.ui.chatscreen.picturequery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import com.learning.mygenai.R
import com.learning.mygenai.model.PhotoChat

class PhotoChatAdapter(var loading:Boolean):ListAdapter<PhotoChat,PhotoQueryViewHolder>(PhotoQueryDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoQueryViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val view=layoutInflater.inflate(R.layout.photo_query_log,parent,false)
        return PhotoQueryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoQueryViewHolder, position: Int) {
        val item=getItem(position)
        Glide.with(holder.photoView).load(item.photoUri.toUri()).placeholder(R.drawable.loading).into(holder.photoView)
        Glide.with(holder.botIcon).load(R.drawable.chatbot).transform(CircleCrop()).into(holder.botIcon)
//        holder.photoView.rotation=90F
        holder.userQuery.text=item.userQuery
        holder.aiResponse.text=item.response
        if(loading && position==itemCount-1) {
            Glide.with(holder.loadingImage).load(R.drawable.loading).into(holder.loadingImage)
            holder.loadingImage.visibility=View.VISIBLE
            holder.aiResponse.visibility=View.INVISIBLE
        }
        else {
            holder.loadingImage.visibility=View.INVISIBLE
            holder.aiResponse.visibility=View.VISIBLE
        }
        val userProfile=FirebaseAuth.getInstance().currentUser?.photoUrl
        userProfile?.let {
             Glide.with(holder.userIcon.context).load(it).transform(CircleCrop()).into(holder.userIcon)
        }
    }
}

class PhotoQueryViewHolder(view: View):RecyclerView.ViewHolder(view) {
    val photoView: ImageView =view.findViewById(R.id.photo)
    val userQuery:TextView=view.findViewById(R.id.user_chat2)
    val aiResponse:TextView=view.findViewById(R.id.ai_response2)
    val userIcon:ImageView=view.findViewById(R.id.user_icon2)
    val botIcon:ImageView=view.findViewById(R.id.bot_icon2)
    val loadingImage:ImageView=view.findViewById(R.id.loading)
}

class PhotoQueryDiffCallBack():DiffUtil.ItemCallback<PhotoChat>() {
    override fun areItemsTheSame(oldItem: PhotoChat, newItem: PhotoChat): Boolean {
        return oldItem.uuid==newItem.uuid
    }

    override fun areContentsTheSame(oldItem: PhotoChat, newItem: PhotoChat): Boolean {
        return oldItem==newItem
    }

}