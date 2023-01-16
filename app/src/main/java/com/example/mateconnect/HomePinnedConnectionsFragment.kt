package com.example.mateconnect

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.UserItemAdapter
import com.example.mateconnect.databinding.FragmentHomePopularConnectionsBinding
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.firestore.FirebaseFirestore

class HomePinnedConnectionsFragment : Fragment() {
    private var _binding: FragmentHomePopularConnectionsBinding? = null

    private val binding get() = _binding!!

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomePopularConnectionsBinding.inflate(inflater, container, false)

    //  reference
    //  https://stackoverflow.com/a/71904931
        val pinnedUsers = CurrentUser.user.pinnedConnections
        val pinnedUsersRef = db.collection("users").whereIn("name", pinnedUsers.toList()).get()
        var users = ArrayList<User>();
        pinnedUsersRef.addOnSuccessListener { result ->
            Log.d("#######", result.documents.toString())
            for (document in result) {
                users.add(document.toObject(User::class.java))
            }
            createRecyclerView(users)
        }
        .addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }

        return binding.root

    }

    private fun createRecyclerView(data: ArrayList<User>) {
        var showPinnedConnections = data.size > 0
        if(data.size == 0) {
            data.add(User("null", "No pinned users yet."))
        }
        val recyclerView: RecyclerView = binding.connectionItemsList
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = UserItemAdapter(data, requireContext(), showAddConnection = false, showPinnedConnection = showPinnedConnections)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
