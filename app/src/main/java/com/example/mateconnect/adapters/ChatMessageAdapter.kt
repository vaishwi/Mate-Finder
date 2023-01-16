package com.example.mateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mateconnect.R
import com.example.mateconnect.models.Message
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.firestore.FirebaseFirestore

class ChatMessageAdapter(val messageList: ArrayList<Message>):RecyclerView.Adapter<ViewHolder>() {

    val ITEM_RECEIVE = 1;
    val ITEM_SENT = 2;

    val loggedInUserUid:String =CurrentUser.user.userId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType==1){
            val view :View = LayoutInflater.from(parent.context).inflate(R.layout.receive_message,parent,false)
            return ReceiveViewHolder(view)
        }
        else{
            val view :View = LayoutInflater.from(parent.context).inflate(R.layout.sent_message,parent,false)
            return SentViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){

            val viewHolder = holder as SentViewHolder

            holder.txtSentMessage.text = currentMessage.message

            holder.txtSentMessageTime.text = "10:50 AM"

        }
        else{

            val viewHolder = holder as ReceiveViewHolder
            holder.txtReceiveMessage.text = currentMessage.message
            holder.txtReceiveMessageTime.text = "10:50 PM"
            var database = FirebaseFirestore.getInstance()
            var userData = database.collection("users")

            userData.whereEqualTo("userId",currentMessage.senderId).get().addOnSuccessListener {
                var name = it.documents[0].data?.get("name").toString()
                holder.txtSenderName.text = name
            }

        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(currentMessage.senderId.equals(loggedInUserUid)){
            return ITEM_SENT
        }
        else{
            return ITEM_RECEIVE
        }
    }

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtSentMessage:TextView = itemView.findViewById(R.id.txt_sent_message)
        val txtSentMessageTime: TextView = itemView.findViewById(R.id.txtSentMessageTime)
    }

    class ReceiveViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtReceiveMessage:TextView = itemView.findViewById(R.id.txt_receive_message)
        val txtReceiveMessageTime: TextView = itemView.findViewById(R.id.txtReceiveMessageTime)
        val txtSenderName:TextView = itemView.findViewById(R.id.txtSenderName)
    }

}