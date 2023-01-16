package com.example.mateconnect


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class InviteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inviteFriendButton = view.findViewById<Button>(R.id.inviteFriendButton)
        val friendTitle = view.findViewById<TextView>(R.id.friendTitle)
        inviteFriendButton.setOnClickListener()
        {
            Snackbar.make(requireView(), "Invitation sent", Snackbar.LENGTH_SHORT)
                .setAnchorView(friendTitle)
                .show()
//            findNavController().navigate(R.id.action_inviteFragment_to_inviteResponseFragment)
        }
    }

}
