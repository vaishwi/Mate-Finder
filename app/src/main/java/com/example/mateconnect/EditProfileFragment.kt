package com.example.mateconnect

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.navigation.NavAction
import androidx.navigation.NavActionBuilder
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap


class EditProfileFragment : Fragment() {

    lateinit var auth: FirebaseFirestore
    var db = FirebaseFirestore.getInstance()
    lateinit var user: User



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var hiddenSkipButton = requireArguments().getString("firstLogin").toString()

        var name = view.findViewById<TextInputEditText>(R.id.name)

        var emailField = view.findViewById<TextInputEditText>(R.id.email)

        var currentAddress = view.findViewById<TextInputEditText>(R.id.currentAddress)

        var sex = view.findViewById<AutoCompleteTextView>(R.id.sex)

        var previousWorkLocation = view.findViewById<TextInputEditText>(R.id.previousWorkLocation)

        var previousWorkOrganisation = view.findViewById<TextInputEditText>(R.id.previousWorkOrganisation)

        var hometown = view.findViewById<TextInputEditText>(R.id.hometown)

        var university = view.findViewById<TextInputEditText>(R.id.university)

        if(hiddenSkipButton.equals("true")){

            toggleSkipButton(true)
            hiddenSkipButton = "false"
        }
        else{

            toggleSkipButton(false)
        }

        val currentUser = CurrentUser.user
        var email = currentUser.email

        val listItemFirst = view.findViewById<AutoCompleteTextView>(R.id.sex)
        val listItemSecond = view.findViewById<AutoCompleteTextView>(R.id.room_preference)
        val listItemThird = view.findViewById<AutoCompleteTextView>(R.id.food_preference)
        val listItemFourth = view.findViewById<AutoCompleteTextView>(R.id.interested_in)


        db.collection("users")
            .document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.toObject(User::class.java)
                    if (data != null) {

                        name.setText(data.name)
                        emailField.setText(data.email)

                        university.setText(data.university)
                        hometown.setText(data.hometown)
                        currentAddress.setText(data.currentAddress)
                        previousWorkLocation.setText(data.previousWorkLocation)
                        previousWorkOrganisation.setText(data.previousWorkOrganisation)


                        listItemFirst.setText(data.sex)
                        val itemsOne = resources.getStringArray(R.array.list_item_one)
                        val arrayAdapterOne: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsOne)
                        listItemFirst.setAdapter(arrayAdapterOne)

                        listItemSecond.setText(data.roomPreference)
                        val itemsTwo = resources.getStringArray(R.array.list_item_two)
                        val arrayAdapterTwo: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsTwo)
                        listItemSecond.setAdapter(arrayAdapterTwo)

                        listItemThird.setText(data.foodPreference)
                        val itemsThree = resources.getStringArray(R.array.list_item_three)
                        val arrayAdapterThree: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsThree)
                        listItemThird.setAdapter(arrayAdapterThree)

                        listItemFourth.setText(data.lookingFor)
                        val itemsFour = resources.getStringArray(R.array.list_item_six)
                        val arrayAdapterFour: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsFour)
                        listItemFourth.setAdapter(arrayAdapterFour)
                    }
                }
            }

        val skipButton = view.findViewById<Button>(R.id.skipButton)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        skipButton.setOnClickListener{

            findNavController().navigate(R.id.homeFragment)

        }

        saveButton.setOnClickListener{

            val user = HashMap<String,Any>()

            user["currentAddress"] = currentAddress.text.toString()
            user["previousWorkLocation"] = previousWorkLocation.text.toString()
            user["previousWorkOrganisation"] = previousWorkOrganisation.text.toString()
            user["hometown"] = hometown.text.toString()
            user["university"] = university.text.toString()
            user["sex"] = listItemFirst.text.toString()
            user["roomPreference"] = listItemSecond.text.toString()
            user["foodPreference"] = listItemThird.text.toString()
            user["lookingFor"] = listItemFourth.text.toString()
            saveUserInformation(email, user)

        }

    }

    private fun toggleSkipButton(show: Boolean) {
        val skipButton = requireActivity().findViewById<Button>(R.id.skipButton)
        skipButton.visibility = if(show) View.VISIBLE else View.GONE

    }

    private fun saveUserInformation(email:String, user:HashMap<String,Any>){

        db.collection("users").document(email)
            .update(user)
            .addOnSuccessListener{
                Snackbar.make(requireView(), "Profile Updated", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

    }


}