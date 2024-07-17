package com.learning.mygenai.ui.chatscreen

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.learning.mygenai.R
import com.learning.mygenai.model.Chat

class ChatAdapter:ListAdapter<Chat,RecyclerView.ViewHolder>(ChatDiffCallBack()) {

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
            holder.userChat.text=getItem(position).chatMessage
        }
        else {
            (holder as ResponseViewHolder).botResponse.text=getItem(position).chatMessage
        }
    }

    class QueryViewHolder(view: View): RecyclerView.ViewHolder(view) {

      //  private val userIcon: ImageView =view.findViewById(R.id.user_icon)
         val userChat: TextView =view.findViewById(R.id.user_chat)

        companion object {
           fun from(parent:ViewGroup): QueryViewHolder {
               val layoutInflater= LayoutInflater.from(parent.context)
               val view=layoutInflater.inflate(R.layout.user_query,parent,false)
               return QueryViewHolder(view)
           }
        }
    }

    class ResponseViewHolder(view: View): RecyclerView.ViewHolder(view) {
      //  private val botIcon: ImageView =view.findViewById(R.id.bot_icon)
         val botResponse: TextView =view.findViewById(R.id.ai_response)

        companion object {
            fun from(parent:ViewGroup): ResponseViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                val view=layoutInflater.inflate(R.layout.ai_response,parent,false)
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