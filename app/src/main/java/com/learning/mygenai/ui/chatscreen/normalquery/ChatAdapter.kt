package com.learning.mygenai.ui.chatscreen.normalquery

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import com.learning.mygenai.R
import com.learning.mygenai.model.Chat

class ChatAdapter(var waiting:Boolean):ListAdapter<Chat,RecyclerView.ViewHolder>(ChatDiffCallBack()) {

//    var data:List<Chat> = listOf()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==0) {
            Log.d("QueryViewHolder","QueryViewHolder")
            QueryViewHolder.from(parent)
        }
        else {
            Log.d("ResponseViewHolder","ResponseViewHolder")
            ResponseViewHolder.from(parent)
        }
    }

//    override fun getItemCount(): Int {
//        return data.size
//    }

    override fun getItemViewType(position: Int): Int {
        return position%2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is QueryViewHolder) {
            val photoUrl=FirebaseAuth.getInstance().currentUser?.photoUrl
            if(photoUrl!=null) {
                Glide.with(holder.userIcon.context).load(photoUrl).transform(CircleCrop()).into(holder.userIcon)
            }
            else {
                holder.userIcon.setImageResource(R.drawable.user_image)
            }
//            else
//                Log.d("CurrUser",FirebaseAuth.getInstance().currentUser!!.photoUrl.toString())
            holder.userChat.text=getItem(position).chatMessage
        }
        else {
            Glide.with((holder as ResponseViewHolder).botIcon).load(R.drawable.chatbot).transform(CircleCrop()).into(holder.botIcon)
            if(waiting && position==itemCount-1 && getItem(position).chatMessage=="We are facing some issue. Please try again.") {
                holder.waiting.isVisible=true
                Glide.with(holder.itemView.context).load(R.drawable.loading).into(holder.waiting)
                holder.botResponse.isVisible=false
            }
            else {
                (holder as ResponseViewHolder).waiting.isVisible=false
                holder.botResponse.isVisible=true
                holder.botResponse.text=getItem(position).chatMessage
            }
        }
    }

         private class QueryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            val userIcon: ImageView = view.findViewById(R.id.user_icon)
            val userChat: TextView = view.findViewById(R.id.user_chat)

            companion object {
                fun from(parent: ViewGroup): QueryViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val view = layoutInflater.inflate(R.layout.user_query, parent, false)
                    return QueryViewHolder(view)
                }
            }
        }

         private class ResponseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            //  private val botIcon: ImageView =view.findViewById(R.id.bot_icon)
            val botResponse: TextView = view.findViewById(R.id.ai_response)
            val waiting: ImageView = view.findViewById(R.id.waiting)
            val botIcon: ImageView = view.findViewById(R.id.bot_icon)

            companion object {
                fun from(parent: ViewGroup): ResponseViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val view = layoutInflater.inflate(R.layout.ai_response, parent, false)
                    return ResponseViewHolder(view)
                }
            }
        }
}

class ChatDiffCallBack():DiffUtil.ItemCallback<Chat>(){
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem==newItem
    }

}