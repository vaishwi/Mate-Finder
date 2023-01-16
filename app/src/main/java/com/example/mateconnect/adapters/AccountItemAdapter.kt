package com.example.mateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.R
import com.example.mateconnect.models.User

class AccountItemAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<AccountItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.account_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user : User = userList[position]
        holder.titleField.text = user.userId
        holder.contentField.text = user.name
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val titleField : TextView = itemView.findViewById(R.id.title_field)
        val contentField : TextView = itemView.findViewById(R.id.content_field)
    }

    fun updateData(userList: ArrayList<User>) {
        userList.clear()
        notifyDataSetChanged()
        userList.addAll(userList)
        notifyDataSetChanged()

    }
}