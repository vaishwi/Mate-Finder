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
import com.example.mateconnect.databinding.FragmentHomeSuggestedConnectionsBinding
import com.example.mateconnect.models.Connection
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class HomeSuggestedConnectionsFragment : Fragment() {
    private var _binding: FragmentHomeSuggestedConnectionsBinding? = null

    private val binding get() = _binding!!

    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeSuggestedConnectionsBinding.inflate(inflater, container, false)

        //  reference
        //  https://stackoverflow.com/a/71904931

        val database = FirebaseDatabase.getInstance().getReference("followers")

        val userName = CurrentUser.user.name
        val connections = database.child(userName).get().addOnSuccessListener { result ->
            var followerEmails = result.value
            followerEmails = listOf(followerEmails)
            var formattedEmail = ArrayList<String>()
            for(email in followerEmails) {
                var emailString = email.toString()
                if(emailString != "null") {
                    formattedEmail.add(emailString.replace(",", "."))
                }
            }
            val nonConnectionsRef = db.collection("users").whereNotIn("email", listOf(formattedEmail)).get()
            var users = ArrayList<User>();
            nonConnectionsRef.addOnSuccessListener { result ->
                for (document in result) {
                    var userObject = document.toObject(User::class.java)
                    if(checkSimilarity(userObject)) {
                        users.add(userObject)
                    }
                }
                createRecyclerView(users)
            }
            .addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }

        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }

        return binding.root

    }

    // firestore can't do or operations. Hence, handling this in client
    private fun checkSimilarity(user: User): Boolean {
        val isSameHomeTown = user.hometown == CurrentUser.user.hometown
        val isSameFoodPreference = user.foodPreference == CurrentUser.user.foodPreference
        val isSameRoomPreference = user.roomPreference == CurrentUser.user.roomPreference
        val isSameSex = user.sex == CurrentUser.user.sex
        return isSameHomeTown || isSameSex || isSameFoodPreference || isSameRoomPreference
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
