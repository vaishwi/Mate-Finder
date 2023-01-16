package com.example.mateconnect

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * Reference - https://www.geeksforgeeks.org/exposed-drop-down-menu-in-android/
 */
class FilterConnectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_connection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listItemFirst = view.findViewById<AutoCompleteTextView>(R.id.listItemOne)
        val itemsOne = resources.getStringArray(R.array.list_item_one)
        val arrayAdapterOne: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsOne)
        listItemFirst.setAdapter(arrayAdapterOne)

        val listItemSecond = view.findViewById<AutoCompleteTextView>(R.id.listItemTwo)
        val itemsTwo = resources.getStringArray(R.array.list_item_two)
        val arrayAdapterTwo: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsTwo)
        listItemSecond.setAdapter(arrayAdapterTwo)

        val listItemThird = view.findViewById<AutoCompleteTextView>(R.id.listItemThree)
        val itemsThree = resources.getStringArray(R.array.list_item_three)
        val arrayAdapterThree: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsThree)
        listItemThird.setAdapter(arrayAdapterThree)

        val listItemFourth = view.findViewById<AutoCompleteTextView>(R.id.listItemFour)
        val itemsFour = resources.getStringArray(R.array.list_item_four)
        val arrayAdapterFour: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsFour)
        listItemFourth.setAdapter(arrayAdapterFour)

        val listItemFifth = view.findViewById<AutoCompleteTextView>(R.id.listItemFive)
        val itemsFive = resources.getStringArray(R.array.list_item_five)
        val arrayAdapterFive: ArrayAdapter<String> = ArrayAdapter(requireContext(),R.layout.list_item,itemsFive)
        listItemFifth.setAdapter(arrayAdapterFive)

        // For Filtering Connections
        val filterResultButton = view.findViewById<Button>(R.id.filter_result_button)
        filterResultButton.setOnClickListener()
        {
//            filterResultButton.text = listItemFirst.text.toString()
            findNavController().navigate(R.id.action_filterConnectionFragment_to_filterResultFragment)
        }
    }
}
