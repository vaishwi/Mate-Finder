package com.example.mateconnect

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.ChatListAdapter
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

const val CHATLIST_PROGRESSBAR_VISIBLE_TEXT :String = "Loading chats.."
const val DATABASE_USERID_KEY = "userId"
const val DATABASE_NAME_KEY = "name"
const val DATABASE_EMAIL_KEY = "email"
const val DATABASE_PASSWORD_KEY = "password"
const val CHAT_LIST_TOPBAR_TEXT = "Chat"
const val DATABASE_USER_COLLECTION_NAME = "users"
const val DATABASE_GROUP_INFO_COLLECTION_NAME = "groupInfo"
const val DATABASE_MEMBERS_KEY = "members"
const val DATABASE_GROUPNAME_KEY = "groupName"
const val GROUP_FLAG = true

class ChatListFragment : Fragment() {

    private lateinit var chatListRecyclerView: RecyclerView
    private lateinit var chatUserList:ArrayList<User>
    private lateinit var chatAdapter: ChatListAdapter

    private lateinit var pbarText:TextView
    private lateinit var pbarLayout: RelativeLayout
    private lateinit var chatFab: View

    var database = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)

        pbarLayout = view.findViewById(R.id.pbarLayoutChatList)
        pbarLayout.setVisibility(View.VISIBLE)
        pbarText = view.findViewById(R.id.txtPbar)
        pbarText.setText(CHATLIST_PROGRESSBAR_VISIBLE_TEXT)

        chatFab = view.findViewById(R.id.chatFab)
        chatFab.setOnClickListener {
            findNavController().navigate(R.id.chatSearchAndChatWithNew)
        }

        chatUserList = ArrayList()
        chatUserList = getChatUserList()

        (activity as AppCompatActivity).supportActionBar?.title = CHAT_LIST_TOPBAR_TEXT

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        chatListRecyclerView = view.findViewById(R.id.chatListRecyclerView)
        chatListRecyclerView.layoutManager = layoutManager
        chatAdapter = ChatListAdapter(chatUserList)
        chatListRecyclerView.adapter = chatAdapter
    }

    private fun getChatUserList(): ArrayList<User> {


         database.collection(DATABASE_USER_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    if(document.data.get(DATABASE_USERID_KEY)!=CurrentUser.user.userId){
                        chatUserList.add(User(document.data.get(DATABASE_USERID_KEY).toString()
                            ,document.data.get(DATABASE_NAME_KEY).toString()
                            ,false
                            ,document.data.get(DATABASE_EMAIL_KEY).toString()
                            ,document.data.get(DATABASE_PASSWORD_KEY).toString()))
                    }
                }

                getGroupDetails()

            }
            .addOnFailureListener { exception ->
                Log.d("DATABASE_ERROR", "Error getting documents: ", exception)
            }

        return chatUserList
    }

    private fun getGroupDetails() {
        database.collection(DATABASE_GROUP_INFO_COLLECTION_NAME)
            .whereArrayContains(DATABASE_MEMBERS_KEY,CurrentUser.user.userId)
            .get()
            .addOnSuccessListener { result->
                for (document in result) {
                    chatUserList.add(User(document.id,document.data.get(DATABASE_GROUPNAME_KEY).toString(),
                        GROUP_FLAG))
                }

                chatAdapter.notifyDataSetChanged()
                pbarText.setText(PROGRESSBAR_INVISIBLE_TEXT)
                pbarLayout.setVisibility(View.INVISIBLE)
            }
            .addOnFailureListener { exception ->
                Log.d("DATABASE_ERROR", "Error getting documents: ", exception)
            }
    }

}