package com.example.mateconnect.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.AddMemberSelectionListener
import com.example.mateconnect.R
import com.example.mateconnect.models.User

class GroupAddMembersAdapter(val connectionList:ArrayList<User>,val addMemberSelectionListener: AddMemberSelectionListener): RecyclerView.Adapter<GroupAddMembersAdapter.GroupAddMembersViewHolder>()  {

    var selectedConnectionList:ArrayList<User> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupAddMembersViewHolder {
        val view :View = LayoutInflater.from(parent.context).inflate(R.layout.group_member_selection_item,parent,false)
        return GroupAddMembersViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupAddMembersViewHolder, position: Int) {

        val currentChatUser = connectionList[position]
        holder.txtChatUserName.text = currentChatUser.name
        holder.checkbox.setOnClickListener {
            if(holder.checkbox.isChecked){
                selectedConnectionList.add(currentChatUser)
            }
            else{
                selectedConnectionList.remove(currentChatUser)
            }
            addMemberSelectionListener.onAddMemberSelectionChange(selectedConnectionList)
        }

    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    class GroupAddMembersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtChatUserName : TextView = itemView.findViewById(R.id.txtChatUserName)
        val checkbox:CheckBox = itemView.findViewById(R.id.checkBoxAddMember)
    }
}