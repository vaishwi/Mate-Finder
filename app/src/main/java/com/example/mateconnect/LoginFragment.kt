package com.example.mateConnect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.example.mateconnect.CHATLIST_PROGRESSBAR_VISIBLE_TEXT
import com.example.mateconnect.HomeFragment
import com.example.mateconnect.R
import com.example.mateconnect.databinding.FragmentLoginBinding
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

const val LOGIN_PROGRESSBAR_VISIBLE_TEXT :String = "Let's begin the fun!"

class LoginFragment : Fragment() {

    var db = FirebaseFirestore.getInstance()

    lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var pbarLayout: RelativeLayout
    private lateinit var pbarText:TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val login = view.findViewById<Button>(R.id.login)

        val signUp = view.findViewById<Button>(R.id.sign_up_button)

        val logo = view.findViewById<TextView>(R.id.loginPlaceholder)


        pbarLayout = view.findViewById(R.id.pbarLoginLayout)
        pbarLayout.setVisibility(View.INVISIBLE)

        pbarText = view.findViewById(R.id.txtPbar)
        pbarText.text = LOGIN_PROGRESSBAR_VISIBLE_TEXT

        login.setOnClickListener {
            pbarLayout.setVisibility(View.VISIBLE)



            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email == "") {
                Snackbar.make(requireView(), "Email is required", Snackbar.LENGTH_SHORT).show()
            } else if (password == "") {
                Snackbar.make(requireView(), "Password is required", Snackbar.LENGTH_SHORT).show()
            } else {

                    logInUser(email, password,logo)
            }
        }

        signUp.setOnClickListener{

            findNavController().navigate(R.id.signUpFragment)


        }


    }

    private fun logInUser(email: String, password: String,logo:TextView){

        var firstLogin = requireArguments().getString("firstSignin").toString()

        db.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.toObject(User::class.java)
                    if (data != null) {
                        if (data.password != password) {
                            Snackbar.make(requireView(), "Email is required", Snackbar.LENGTH_SHORT)
                                .show()
                        } else {
                            CurrentUser.user = data
                            Snackbar.make(requireView(), "Logged in successfully!", Snackbar.LENGTH_SHORT)
                                .setAnchorView(logo)
                                .show()
                            if (firstLogin.equals("true")) {
                                findNavController().navigate(R.id.editProfileFragment, Bundle().apply {
                                        putString("firstLogin", "true")
                                        firstLogin = "false"
                                    })
                            } else {
                                findNavController().navigate(R.id.homeFragment, Bundle().apply {
                                    putString("email", email)
                                })
                            }
                        }
                    } else {
                        Snackbar.make(
                            requireView(),
                            "Email Address does not exist",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Snackbar.make(requireView(), "Some error occurred. Please, try again.", Snackbar.LENGTH_SHORT).show()
            }

    }

    private fun toggleActionBarAndBottomNavigation(show: Boolean) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = if(show) View.VISIBLE else View.GONE
        val actionBarLayout = requireActivity().findViewById<AppBarLayout>(R.id.appbar_layout)
        actionBarLayout.visibility = if(show) View.VISIBLE else View.GONE
    }

}

