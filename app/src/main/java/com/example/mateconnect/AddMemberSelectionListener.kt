package com.example.mateconnect

import com.example.mateconnect.models.User

interface AddMemberSelectionListener {
    fun onAddMemberSelectionChange(selctedMemberList:ArrayList<User> )
}