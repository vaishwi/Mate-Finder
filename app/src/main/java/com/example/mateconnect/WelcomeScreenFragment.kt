package com.example.mateconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mateconnect.databinding.FragmentWelcomeScreenBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class WelcomeScreenFragment : Fragment() {
    private var _binding: FragmentWelcomeScreenBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun toggleActionBarAndBottomNavigation(show: Boolean) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = if(show) View.VISIBLE else View.GONE
        val actionBarLayout = requireActivity().findViewById<AppBarLayout>(R.id.appbar_layout)
        actionBarLayout.visibility = if(show) View.VISIBLE else View.GONE
    }

//    reference
//    https://stackoverflow.com/a/63307603
    override fun onStart() {
        super.onStart()
        toggleActionBarAndBottomNavigation(false)
    }

    override fun onStop() {
        super.onStop()
        toggleActionBarAndBottomNavigation(true)
    }


    override fun onResume() {
        super.onResume()
        toggleActionBarAndBottomNavigation(false)
    }

    //

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val login = binding.signInButton
        val signup = binding.registerButton

        login.setOnClickListener{
            findNavController().navigate(R.id.loginFragment)
        }

        signup.setOnClickListener{
            findNavController().navigate(R.id.signUpFragment)
        }
    }
}