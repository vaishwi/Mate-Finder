package com.example.mateconnect

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.ChatMessageAdapter
import com.example.mateconnect.models.Message
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val CHATROOM_PROGRESSBAR_VISIBLE_TEXT :String = "Loading Messages.."
const val PROGRESSBAR_INVISIBLE_TEXT :String = "Loading.."

class ChatRoomFragment : Fragment() {

    private lateinit var chatRoomRecyclerView:RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sendButton:ImageView
    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private lateinit var messageList: ArrayList<Message>

    private lateinit var pbarText: TextView
    private lateinit var pbarLayout: RelativeLayout

    private var database = FirebaseFirestore.getInstance()
    private var chatCollectionData = database.collection("chatMessages")

    var senderId:String? = null
    var receiverId:String? = null
    var chatId:String? = null
    private lateinit var chatUser: User

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view  = inflater.inflate(R.layout.fragment_chat_room, container, false)

        chatRoomRecyclerView = view.findViewById(R.id.chatRoomRecyclerView)
        messageBox = view.findViewById(R.id.messageBox)
        sendButton = view.findViewById(R.id.sentButton)

        pbarLayout = view.findViewById(R.id.pbarLayoutChatRoom)
        pbarLayout.setVisibility(View.VISIBLE)
        pbarText = view.findViewById(R.id.txtPbar)
        pbarText.setText(CHATROOM_PROGRESSBAR_VISIBLE_TEXT)

        val args: ChatRoomFragmentArgs by navArgs()

        messageList = ArrayList()
        chatUser = args.chatUser
        receiverId = chatUser.userId

        senderId = CurrentUser.user.userId


        (activity as AppCompatActivity).supportActionBar?.title = chatUser.name
        messageList = ArrayList()
        getMessages(senderId.toString(),receiverId.toString())

        sendButton.setOnClickListener {

            var message:String = messageBox.text.toString()

            storeMessageInDatabase(message, senderId!!, receiverId!!)
            messageBox.setText("")
        }


        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        Log.d("CURRENT_TIME",formatted)

//        String.substring(startIndex: Int, endIndex: Int)
        Log.d("CURRENT_TIME",formatted.toString().substring(12,18))
//
//        Log.d("TIME_FROM",formatted.get)

        var timeStamp = java.sql.Timestamp(System.currentTimeMillis()).toString()
        Log.d("TIME",Timestamp.valueOf(timeStamp).getTime().toString())

        val dt = Date(Timestamp.valueOf(timeStamp).getTime())
        val sdf = SimpleDateFormat("hh:mm aa")
        val time1: String = sdf.format(dt)
        Log.d("TIME",time1)

        // Inflate the layout for this fragment
        return view
    }

    private fun getMessages(senderId:String, receiverId:String): ArrayList<Message> {

        messageList.clear()
        Log.d("GET_MESSAGESS_CHATID",receiverId)
        if(chatUser.isGroup){
            // for group chat messages
            chatCollectionData.whereEqualTo("chatId",receiverId).get()
                .addOnSuccessListener {
                    for (document in it) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        var messages: ArrayList<HashMap<String, Any>> =
                            document.data.get("messages") as ArrayList<HashMap<String, Any>>

                        for (message in messages) {
                            messageList.add(
                                Message(
                                    message.get("message").toString(),
                                    message.get("senderId").toString(),
                                    message.get("timeStamp").toString()
                                )
                            )
                        }
                    }
                    chatMessageAdapter.notifyDataSetChanged()
                    pbarText.setText(PROGRESSBAR_INVISIBLE_TEXT)
                    pbarLayout.setVisibility(View.INVISIBLE)
                }
        }
        else{
            chatCollectionData
                .whereIn("chatId", listOf(senderId+"_"+receiverId,receiverId+"_"+senderId))
                .get().addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("TAG", "${document.id} => ${document.data}")
                        var messages : ArrayList<HashMap<String,Any>> = document.data.get("messages") as ArrayList<HashMap<String,Any>>

                        for (message in messages){
                            messageList.add(
                                Message(message.get("message").toString()
                                    ,message.get("senderId").toString()
                                    ,message.get("timeStamp").toString()))
                        }
                    }
                    chatMessageAdapter.notifyDataSetChanged()
                    pbarText.setText(PROGRESSBAR_INVISIBLE_TEXT)
                    pbarLayout.setVisibility(View.INVISIBLE)
            }

        }

        return messageList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun storeMessageInDatabase(message: String, senderId: String, receiverId: String) {

        var messages: ArrayList<Map<String,Any>>? = null
        var success = false
        if(chatUser.isGroup){
            val groupId = receiverId
            chatCollectionData.whereEqualTo("chatId",groupId).get()
                .addOnSuccessListener {result->
                    if(!result.isEmpty){
                        chatId = result.documents[0].data?.get("chatId").toString()
                        messages =  result.documents[0].data?.get("messages") as ArrayList<Map<String, Any>>

                        updateMessagesInDatabase(chatId!!,message,senderId, messages!!)
                        success = true
                        getMessages(senderId, receiverId)
                    }
                }.addOnFailureListener{
                    Log.d("DATABASE_ERROR", "Error getting documents: ")
                }
        }
        else {
            chatCollectionData
                .whereIn("chatId", listOf(senderId + "_" + receiverId, receiverId + "_" + senderId))
                .get().addOnSuccessListener { result ->
                    if (result.isEmpty) {

                        chatId = senderId + "_" + receiverId
                        messages = ArrayList<Map<String, Any>>()
                    } else {

                        chatId = result.documents[0].data?.get("chatId").toString()
                        messages =
                            result.documents[0].data?.get("messages") as ArrayList<Map<String, Any>>

                    }
                    updateMessagesInDatabase(chatId!!,message,senderId, messages!!)
                    getMessages(senderId, receiverId)
                    success = true
                }
                .addOnFailureListener{
                    Log.d("DATABASE_ERROR", "Error getting documents: ")
                }
        }

    }

    private fun updateMessagesInDatabase(chatId: String, message: String, senderId: String, messages: ArrayList<Map<String, Any>>) {
        var timeStamp = java.sql.Timestamp(System.currentTimeMillis()).toString()
        var messageObject = mapOf(
            "message" to message,
            "senderId" to senderId,
            "timeStamp" to timeStamp
        )
        var currentMessage: Map<String, Any> = messageObject
        messages!!.add(currentMessage)

        var chatData = mapOf("chatId" to chatId, "messages" to messages)
        chatCollectionData.document(chatId.toString()).set(chatData)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        chatRoomRecyclerView = view.findViewById(R.id.chatRoomRecyclerView)
        chatRoomRecyclerView.layoutManager = layoutManager
        chatMessageAdapter = ChatMessageAdapter(messageList)
        chatRoomRecyclerView.adapter = chatMessageAdapter
    }

}


