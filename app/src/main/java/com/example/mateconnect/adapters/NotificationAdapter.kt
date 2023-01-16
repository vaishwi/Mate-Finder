package com.example.mateconnect.adapters

//references:
//https://stackoverflow.com/a/54655654
//https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/
//assignment-3

import android.content.Context
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


class NotificationAdapter(
    private var userList: ArrayList<Connection>,
    private var showIcon: Boolean = true,
    private val context: Context) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.noti_user_name)
        var acceptItemIcon: MaterialButton = view.findViewById(R.id.accept_item_button)
        var declineItemIcon: MaterialButton = view.findViewById(R.id.decline_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text =  userList[position].name

        var database : DatabaseReference
        database = FirebaseDatabase.getInstance().getReference("followers")

        var database1 : DatabaseReference
        database1 = FirebaseDatabase.getInstance().getReference("requests")

        var userName= CurrentUser.user.name
        var friend = holder.name.text

        if (!showIcon) {
            holder.acceptItemIcon.visibility = View.GONE
            holder.declineItemIcon.visibility = View.GONE
        }

        fun deleteItem(position: Int) {
            userList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, userList.size)
            holder.itemView.visibility = View.GONE
        }

        holder.acceptItemIcon.setOnClickListener {
            var count = 0
            database.child(userName).get().addOnSuccessListener { result ->
                for (document in result.key.toString()) {
                    count+=1
                }
            }
            database.child(userName).child(count.toString()).setValue(friend).addOnSuccessListener {
                deleteItem(position)
            }.addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
            database.child(friend.toString()).child(count.toString()).setValue(userName).addOnSuccessListener {
                Log.i("Connection", "writing in friend...")
            }.addOnFailureListener { exception ->
                Log.w("Error", "Error getting documents: ", exception)
            }
        }

        holder.declineItemIcon.setOnClickListener {
            deleteItem(position)
            database1.child(userName).child(position.toString()).removeValue().addOnSuccessListener {
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
