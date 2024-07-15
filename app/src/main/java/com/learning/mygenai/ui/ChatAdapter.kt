package com.learning.mygenai.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.learning.mygenai.R

class ChatAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==0) QueryViewHolder.from(parent)
        else ResponseViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return position%2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class QueryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val userIcon: ImageView =view.findViewById(R.id.user_icon)
        private val userChat: TextView =view.findViewById(R.id.user_chat)

        companion object {
           fun from(parent:ViewGroup):QueryViewHolder {
               val layoutInflater= LayoutInflater.from(parent.context)
               val view=layoutInflater.inflate(R.layout.user_query,parent,false)
               return QueryViewHolder(view)
           }
        }
    }

    class ResponseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val botIcon: ImageView =view.findViewById(R.id.bot_icon)
        private val botResponse: TextView =view.findViewById(R.id.ai_response)

        companion object {
            fun from(parent:ViewGroup):QueryViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                val view=layoutInflater.inflate(R.layout.ai_response,parent,false)
                return QueryViewHolder(view)
            }
        }
    }

}