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
import com.example.mateconnect.databinding.FragmentFilterResultBinding
import com.example.mateconnect.databinding.FragmentHomeAllConnectionsBinding
import com.example.mateconnect.models.User
import com.google.firebase.firestore.FirebaseFirestore


class FilterResultFragment : Fragment() {

    private var _binding: FragmentFilterResultBinding? = null

    private val binding get() = _binding!!

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilterResultBinding.inflate(inflater, container, false)

//        reference
//        https://stackoverflow.com/a/71904931
        val allUsersRef = db.collection("users").get()
        var users = ArrayList<User>();
        allUsersRef.addOnSuccessListener { result ->
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
        val recyclerView: RecyclerView = binding.connectionItemsList
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = UserItemAdapter(data, requireContext(), showAddConnection = true, showPinnedConnection = false)
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
