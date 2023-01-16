package com.example.mateconnect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.ConnectionAdapter
import com.example.mateconnect.databinding.FragmentConnectionsBinding
import com.example.mateconnect.models.Connection
import com.example.mateconnect.state.CurrentUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ConnectionsFragment : Fragment() {
    private var _binding: FragmentConnectionsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentConnectionsBinding.inflate(inflater, container, false)

        val users = ArrayList<Connection>();
        val users1 = ArrayList<Connection>();

        var userName= CurrentUser.user.name

        var database : DatabaseReference
        database = FirebaseDatabase.getInstance().getReference("followers")

        val allUsersRef = database.child(userName).get().addOnSuccessListener { result ->
            users.add(Connection(result.value.toString()))
            users.forEach {
                val temp=it.name.replace("[","").replace("]","").split(",")
                var i :Int = temp.size
                while (i>0){
                    i-=1
                    if(temp[i] != null && temp[i] != "null") {
                        users1.add(Connection(temp[i]))
                    }
                }
            }
            createRecyclerView(users1)
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }
        return binding.root

    }

    private fun createRecyclerView(data: ArrayList<Connection>) {
        val showIcons = data.size > 0
        if(data.size == 0) {
            data.add(Connection("No connection yet."))
        }
        val recyclerView: RecyclerView = binding.connections
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = ConnectionAdapter(data, showIcons, requireContext())
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
