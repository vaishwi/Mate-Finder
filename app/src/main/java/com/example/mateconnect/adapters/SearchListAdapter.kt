package com.example.mateconnect.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.ChatListFragmentDirections
import com.example.mateconnect.chatSearchAndChatWithNewDirections
import com.example.mateconnect.R
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SearchListAdapter(
    val chatUserList:ArrayList<User>,
    private val context: Context) : RecyclerView.Adapter<SearchListAdapter.ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
       val view :View = LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)
        return ChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {

        val currentChatUser = chatUserList[position]
        Log.d("CURRENTUSER",currentChatUser.toString())

        holder.txtChatUserName.text = currentChatUser.name
        holder.userItemIcon.setOnClickListener {
            // add the person to the current user's connection list
            var userName= CurrentUser.user.name
            var database : DatabaseReference
            database = FirebaseDatabase.getInstance().getReference("requests")
            var friend = holder.txtChatUserName.text
            database.child(friend as String).setValue(userName).addOnSuccessListener {
                holder.userItemIcon.icon = ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.ic_check_foreground,
                    null
                )
            }
        }

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
        val txtChatUserName :TextView= itemView.findViewById(R.id.user_name)
        var userItemIcon: MaterialButton = itemView.findViewById(R.id.user_item_button)
    }

}
