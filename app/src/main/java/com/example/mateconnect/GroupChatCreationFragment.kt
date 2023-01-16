package com.example.mateconnect

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
//import androidx.appcompat.widget.SearchView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.GroupAddMembersAdapter
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList


class GroupChatCreationFragment : Fragment() ,AddMemberSelectionListener{

    private lateinit var txtGroupName:TextView
    private lateinit var txtGroupDescription:TextView
    private lateinit var btnAddMembers:Button
    private lateinit var searchViewCreateGroup:SearchView
    private lateinit var btnCreateGropup:Button
    private lateinit var createGroupRecyclerView: RecyclerView
    private lateinit var groupAddMembersAdapter: GroupAddMembersAdapter

    private lateinit var connectionList:ArrayList<User>
    private lateinit var groupMembersList:ArrayList<User>

    private var database = FirebaseFirestore.getInstance()
    private var chatCollectionData = database.collection("chatMessages")
    private var groupCollectionData = database.collection("groupInfo")

    private lateinit var filteredConnectionList:ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_group_chat_creation, container, false)

        txtGroupName = view.findViewById(R.id.txtGroupName)
        txtGroupDescription = view.findViewById(R.id.txtGroupDescription)
        btnAddMembers = view.findViewById(R.id.btnAddMembers)
        btnCreateGropup = view.findViewById(R.id.btnCreateGropup)
        searchViewCreateGroup = view.findViewById(R.id.searchViewCreateGroup)
        searchViewCreateGroup.setVisibility(View.INVISIBLE)
        connectionList = ArrayList()
        groupMembersList = ArrayList()
        filteredConnectionList = ArrayList()
        connectionList = getConnectionList()


        btnAddMembers.setOnClickListener{
            createGroupRecyclerView.setVisibility(View.VISIBLE)
            searchViewCreateGroup.setVisibility(View.VISIBLE)
        }

        btnCreateGropup.setOnClickListener {
            val grpName = txtGroupName.text.toString()
            val grpDescription = txtGroupDescription.text.toString()

            storeGroupInDatabase(grpName,grpDescription,groupMembersList)
        }

        (activity as AppCompatActivity).supportActionBar?.title = "Group Creation"


        searchViewCreateGroup.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchViewCreateGroup.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val searchText  =newText!!.lowercase(Locale.getDefault())

                filteredConnectionList.clear()
                if(searchText.isNotEmpty()){
                    connectionList.forEach {

                        if(it.name.lowercase(Locale.getDefault()).contains(searchText)){
                            filteredConnectionList.add(it)
                        }
                    }
                }
                else{
                    filteredConnectionList.addAll(connectionList)
                }

                groupAddMembersAdapter.notifyDataSetChanged()

                return false
            }

        })


        // Inflate the layout for this fragment
        return view
    }

    private fun storeGroupInDatabase(grpName: String, grpDescription: String, groupMembersList: ArrayList<User>) {
        var membersId = ArrayList<String>()

        groupMembersList.forEach {
            membersId.add(it.userId)
        }
        membersId.add(CurrentUser.user.userId)

        val groupData = mapOf("groupName" to grpName, "grpDesc" to grpDescription,"members" to membersId)

        groupCollectionData.add(groupData).addOnSuccessListener {
            Toast.makeText(context,"Group successfully added!", Toast.LENGTH_LONG).show()
            val emptyMessageList:ArrayList<String> = ArrayList()
            var chatMessageData = mapOf("chatId" to it.id, "isGroup" to true,"messages" to emptyMessageList)
            chatCollectionData.document(it.id.toString()).set(chatMessageData)

            findNavController().navigate(R.id.chatListFragment)
        }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error creating group document", e) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        createGroupRecyclerView = view.findViewById(R.id.createGroupRecyclerView)
        createGroupRecyclerView.layoutManager = layoutManager
        groupAddMembersAdapter = GroupAddMembersAdapter(filteredConnectionList,this)
        createGroupRecyclerView.adapter = groupAddMembersAdapter

        createGroupRecyclerView.setVisibility(View.INVISIBLE)
    }

    private fun getConnectionList(): ArrayList<User> {
        connectionList.clear()
        var database = FirebaseFirestore.getInstance()
        database.collection(DATABASE_USER_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    if(document.data.get(DATABASE_USERID_KEY)!= CurrentUser.user.userId){
                        connectionList.add(User(document.data.get(DATABASE_USERID_KEY).toString()
                            ,document.data.get(DATABASE_NAME_KEY).toString()
                            ,false
                            ,document.data.get(DATABASE_EMAIL_KEY).toString()
                            ,document.data.get(DATABASE_PASSWORD_KEY).toString()))
                    }
                }
                filteredConnectionList.addAll(connectionList)
                groupAddMembersAdapter.notifyDataSetChanged()
//                pbarText.setText(PROGRESSBAR_INVISIBLE_TEXT)
//                pbarLayout.setVisibility(View.INVISIBLE)
            }
            .addOnFailureListener { exception ->
                Log.d("DATABASE_ERROR", "Error getting documents: ", exception)
            }
        return connectionList
    }

    override fun onAddMemberSelectionChange(selctedMemberList: ArrayList<User>) {
        groupMembersList = selctedMemberList
        Log.d("SELECTED_ITEMS",groupMembersList.toString())
    }
}