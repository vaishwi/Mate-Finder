package com.example.mateConnect

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mateconnect.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class SignUpFragment : Fragment() {

    lateinit var auth: FirebaseFirestore
    var userUUID = UUID.randomUUID().toString()
    var db = FirebaseFirestore.getInstance()



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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val name = view.findViewById<EditText>(R.id.name)

        val email = view.findViewById<EditText>(R.id.email)

        val password = view.findViewById<EditText>(R.id.password)

        val confirmPassword = view.findViewById<EditText>(R.id.confirmPassword)

        val button = view.findViewById<Button>(R.id.signup)

        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)

        val signIn = view.findViewById<Button>(R.id.signInButton)

        button.setOnClickListener {

                if(name.text.toString() == "" || name.text.toString().isDigitsOnly()){
                    Snackbar.make(requireView(),"Invalid name", Snackbar.LENGTH_SHORT).show()
                    onResume()
                }
                else if(email.text.toString() == ""){
                    Snackbar.make(requireView(),"Email address cannot be blank", Snackbar.LENGTH_SHORT).show()
                    onResume()
                }
                else if(password.text.toString() == ""){
                    Snackbar.make(requireView(),"Password cannot be blank", Snackbar.LENGTH_SHORT).show()
                    onResume()
                }
                else if(password.text.toString() == confirmPassword.text.toString()) {

                    if(checkBox.isChecked) {
                    findNavController().navigate(R.id.loginFragment, Bundle().apply {
                        putString("firstSignin","true")
                    })
                        signUpUser(
                            userUUID,
                            name.text.toString(),
                            email.text.toString(),
                            password.text.toString()
                        )
                    }
                    else{
                        Snackbar.make(requireView(),"Please accept Terms & Conditions", Snackbar.LENGTH_SHORT).show()
                    }
                }
                else if(password.text.toString() != confirmPassword.text.toString()){
                    Snackbar.make(requireView(),"Passwords don't match", Snackbar.LENGTH_SHORT).show()
                    onResume()
                }

        }

        signIn.setOnClickListener{

                findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun signUpUser(userId: String, name: String, email: String, password: String) {
        val user = HashMap<String,Any>()

        user["userId"] = userId
        user["name"] = name
        user["email"] = email
        user["password"] = password


        db.collection("users").document(email).set(user)
            .addOnSuccessListener {
                Toast.makeText(context,"Profile successfully added!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }

    private fun toggleActionBarAndBottomNavigation(show: Boolean) {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = if(show) View.VISIBLE else View.GONE
        val actionBarLayout = requireActivity().findViewById<AppBarLayout>(R.id.appbar_layout)
        actionBarLayout.visibility = if(show) View.VISIBLE else View.GONE
    }

}