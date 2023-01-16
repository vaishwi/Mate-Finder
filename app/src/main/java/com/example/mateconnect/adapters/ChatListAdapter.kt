package com.example.mateconnect.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.ChatListFragmentDirections
import com.example.mateconnect.chatSearchAndChatWithNewDirections
import com.example.mateconnect.R
import com.example.mateconnect.models.User

class ChatListAdapter(val chatUserList:ArrayList<User>) : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
       val view :View = LayoutInflater.from(parent.context).inflate(R.layout.chat_user_item,parent,false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {

        val currentChatUser = chatUserList[position]
        Log.d("CURRENTUSER",currentChatUser.toString())

        holder.txtChatUserName.text = currentChatUser.name

        holder.itemView.setOnClickListener{


            if(findNavController(holder.itemView).currentDestination?.id==R.id.chatListFragment){
                findNavController(holder.itemView).navigate(ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(currentChatUser))
            }
            else{
                findNavController(holder.itemView).navigate(chatSearchAndChatWithNewDirections.actionChatSearchAndChatWithNewToChatRoomFragment(currentChatUser))
            }

        }

    }

    override fun getItemCount(): Int {
       return chatUserList.size
    }

    class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtChatUserName :TextView= itemView.findViewById(R.id.txtChatUserName)
    }

}