package com.example.mateconnect.adapters

//references:
//https://stackoverflow.com/a/54655654
//https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/
//assignment-3

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.R
import com.example.mateconnect.models.Connection
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class ConnectionAdapter(
    private val userList: ArrayList<Connection>,
    private val showIcon: Boolean = true,
    private val context: Context) : RecyclerView.Adapter<ConnectionAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.connection_user_name)
        var unfollow: MaterialButton = view.findViewById(R.id.unfollow)
        var pinUnpin: MaterialButton = view.findViewById(R.id.pin_unpin)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.fragment_connection_items, parent, false))
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var database1 : DatabaseReference
        database1 = FirebaseDatabase.getInstance().getReference("followers")
        var db = FirebaseFirestore.getInstance()

        var userName= CurrentUser.user.name
        var friend = holder.name.text

        holder.name.text =  userList[position].name
        if (!showIcon) {
            holder.unfollow.visibility = View.GONE
            holder.pinUnpin.visibility = View.GONE
        }

        holder.name.setOnClickListener {
            //View profile details
            Log.i("Hit", "Hit")
        }

        fun deleteItem(position: Int) {
            userList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, userList.size)
            holder.itemView.visibility = View.GONE
        }

        holder.pinUnpin.setOnClickListener {
            val formattedEmail = userList[position].name
            val removingPin = CurrentUser.user.pinnedConnections.contains(formattedEmail)
            if(removingPin) {
                CurrentUser.user.pinnedConnections.remove(formattedEmail)
            } else {
                CurrentUser.user.pinnedConnections.add(formattedEmail)
            }
            db.collection("users").document(CurrentUser.user.email).set(CurrentUser.user).addOnSuccessListener {
                if(removingPin) {
                    holder.pinUnpin.iconTint = ColorStateList.valueOf(Color.GRAY)
                } else {
                    holder.pinUnpin.iconTint = ColorStateList.valueOf(R.color.primary_color)
                }
            }
        }

        holder.unfollow.setOnClickListener {
            deleteItem(position)
            database1.child(userName).child(position.toString()).removeValue().addOnSuccessListener {
                Log.i("Deleted", "Deleted")
            }.addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
            database1.child(friend.toString()).child(position.toString()).removeValue().addOnSuccessListener {
                Log.i("Deleted", "Deleted")
            }.addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
