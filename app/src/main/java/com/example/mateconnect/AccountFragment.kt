package com.example.mateconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mateconnect.adapters.AccountItemAdapter
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser

/**
 * Fragment for getting and displaying the account details
 */
class AccountFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList : ArrayList<User>
    private lateinit var accountAdapter : AccountItemAdapter

    override fun onStart() {
        super.onStart()
        toggleSignOutMessageButton(true)
    }

    override fun onStop() {
        super.onStop()
        toggleSignOutMessageButton(false)
    }

    override fun onResume() {
        super.onResume()
        toggleSignOutMessageButton(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAccountDetails()
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        accountAdapter = AccountItemAdapter(userList)
        accountAdapter.notifyDataSetChanged()
        recyclerView.adapter = accountAdapter

        // For Inviting Friend
        val inviteButton = view.findViewById<Button>(R.id.inviteButton)
        inviteButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_accountFragment_to_inviteFragment)
        }

        // For Reporting Bug
        val reportButton = view.findViewById<Button>(R.id.reportBugButton)
        reportButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_accountFragment_to_bugReportFragment)
        }

        //For Editing Profile
        val editProfileButton = view.findViewById<Button>(R.id.editProfileButton)
        editProfileButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_accountFragment_to_editProfileFragment)
        }
    }

    // For Getting Profile Details
    private fun getAccountDetails(): ArrayList<User> {
        userList = ArrayList()
        val user = CurrentUser.user
        val titlesList : Array<String> = arrayOf("Name","Email","Address","Sex","University","HomeTown","Previous Work Location",
                                                "Previous Work Organisation","Room Preference","Looking For","Food Preference")
        val valuesList : Array<String> = arrayOf(user.name,user.email,user.currentAddress,user.sex,user.university,user.hometown,
                                                user.previousWorkLocation,user.previousWorkOrganisation,user.roomPreference,
                                                user.lookingFor,user.foodPreference)
        titlesList.forEachIndexed { index, _ ->
            userList.add(User(titlesList[index],valuesList[index]))
        }
        return userList
    }

    private fun toggleSignOutMessageButton(show: Boolean) {
        val signOut = requireActivity().findViewById<Button>(R.id.signOut)
        signOut.visibility = if(show) View.VISIBLE else View.GONE
        val messageButton = requireActivity().findViewById<Button>(R.id.message_button)
        messageButton.visibility = if(!show) View.VISIBLE else View.GONE
    }

}
