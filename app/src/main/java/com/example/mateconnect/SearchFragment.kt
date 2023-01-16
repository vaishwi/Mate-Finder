package com.example.mateconnect

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.UserItemAdapter
import com.example.mateconnect.databinding.ActivityMainBinding
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    val SEARCH_TITLE ="Select connection"

    private lateinit var connectionListRecyclerView: RecyclerView
    private lateinit var connectionList:ArrayList<User>
    private lateinit var connectionListAdapter: UserItemAdapter

    private lateinit var filteredConnectionList:ArrayList<User>

    private lateinit var binding: ActivityMainBinding

    private lateinit var searchView:SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        connectionList = ArrayList()
        filteredConnectionList = ArrayList()
        connectionList = getChatUserList()

        searchView = view.findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
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

                connectionListAdapter.notifyDataSetChanged()

                return false
            }

        })

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        binding.messageButton.setVisibility(View.INVISIBLE)

        (activity as AppCompatActivity).supportActionBar?.title = SEARCH_TITLE
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For Filter Connections
        val filterConnectionButton = view.findViewById<Button>(R.id.filterConnectionButton)
        filterConnectionButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_searchFragment_to_filterConnectionFragment)
        }
        val layoutManager = LinearLayoutManager(context)
        connectionListRecyclerView = view.findViewById(R.id.connectionListRecyclerview)
        connectionListRecyclerView.layoutManager = layoutManager
        connectionListAdapter = UserItemAdapter(filteredConnectionList, requireContext(), showAddConnection = true, showPinnedConnection = false)
        connectionListRecyclerView.adapter = connectionListAdapter
    }


    private fun getChatUserList(): ArrayList<User> {
        filteredConnectionList.clear()
        var database = FirebaseFirestore.getInstance()
        database.collection(DATABASE_USER_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    if(document.data.get(DATABASE_USERID_KEY)!= CurrentUser.user.userId){
                        connectionList.add(User(document.data.get(DATABASE_USERID_KEY).toString()
                            ,document.data.get(DATABASE_NAME_KEY).toString()
                            ,false
                            ,document.data.get(DATABASE_PASSWORD_KEY).toString()))
                    }
                }
                filteredConnectionList.addAll(connectionList)
                connectionListAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("DATABASE_ERROR", "Error getting documents: ", exception)
            }
        return connectionList
    }


}
