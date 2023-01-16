//references:
//https://stackoverflow.com/a/54655654
//https://www.geeksforgeeks.org/android-recyclerview-in-kotlin/
//assignment-3

package com.example.mateconnect.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.R
import com.example.mateconnect.models.Connection
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap


class UserItemAdapter  (
    private val userList: ArrayList<User>,
    private val context: Context,
    private val showAddConnection: Boolean=false,
    private val showPinnedConnection: Boolean=false) : RecyclerView.Adapter<UserItemAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.user_name)
        var userItemIcon: MaterialButton = view.findViewById(R.id.user_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(!showPinnedConnection && !showAddConnection) {
            holder.userItemIcon.visibility = View.GONE
        }

        holder.name.text =  userList[position].name
        if(showAddConnection) {
            holder.userItemIcon.icon = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_baseline_person_add_alt_1_24,
                null
            )
        } else if(showPinnedConnection) {
            holder.userItemIcon.icon = ResourcesCompat.getDrawable(
                context.resources,
                R.drawable.ic_baseline_push_pin_24,
                null
            )
        }


        fun deleteItem(position: Int) {
            userList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, userList.size)
            holder.itemView.visibility = View.GONE
        }


        holder.userItemIcon.setOnClickListener {
            if(showAddConnection) {
                // add the person to the current user's connection list
                var userName= CurrentUser.user.name
                var friend : String = userList[position].name

                var database : DatabaseReference
                database = FirebaseDatabase.getInstance().getReference("requests")

                var count :Int = 0

                val users = ArrayList<Connection>();

                database.child(friend).get().addOnSuccessListener { result ->
                    users.add(Connection(result.value.toString()))
                    users.forEach {
                        val temp = it.name.replace("[", "").replace("]", "").split(",")
                        count = temp.size
                    }
                    database.child(friend).child(count.toString()).setValue(userName).addOnSuccessListener {
                        holder.userItemIcon.icon = ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_check_foreground,
                            null
                        )
                    }
                }.addOnFailureListener { exception ->
                    Log.w("Error", "Error getting documents: ", exception)
                }
            } else if(showPinnedConnection) {
                var db = FirebaseFirestore.getInstance()
                CurrentUser.user.pinnedConnections.remove(userList[position].email.replace(",", "."))
                db.collection("users").document(CurrentUser.user.email).set(CurrentUser.user).addOnSuccessListener {
                    deleteItem(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(userList: ArrayList<User>) {
        userList.clear()
        notifyDataSetChanged()
        userList.addAll(userList)
        notifyDataSetChanged()

    }

}
