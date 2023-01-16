package com.example.mateconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.mateconnect.adapters.HomePageTabsViewPagerAdapter
import com.example.mateconnect.databinding.FragmentHomeBinding
import com.example.mateconnect.models.User
import com.example.mateconnect.state.CurrentUser
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore

val tabTextsArray = arrayOf(
    "All",
    "Suggested",
    "Pinned"
)

val tabIconsArray = arrayOf(
    R.drawable.ic_baseline_public_24,
    R.drawable.ic_baseline_tips_and_updates_24,
    R.drawable.ic_baseline_local_fire_department_24
)

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.homePageTabsViewPager
        val tabLayout = binding.homePageTabLayout

        getGreetingMessage(view)

        val adapter = activity?.let {
                    HomePageTabsViewPagerAdapter(
                        it.supportFragmentManager,
                        lifecycle
                    )
                }
                viewPager.adapter = adapter

                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                    tab.text = tabTextsArray[position]
                    when (position) {
                        0 -> {
                            tab.icon = ResourcesCompat.getDrawable(
                                requireActivity().resources,
                                R.drawable.ic_baseline_public_24,
                                null
                            )
                        }
                        1 -> {
                            tab.icon = ResourcesCompat.getDrawable(
                                requireActivity().resources,
                                R.drawable.ic_baseline_tips_and_updates_24,
                                null
                            )
                        }
                        2 -> {
                            tab.icon = ResourcesCompat.getDrawable(
                                requireActivity().resources,
                                R.drawable.ic_baseline_local_fire_department_24,
                                null
                            )
                        }
                    }
                }.attach()
            }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    var userName = ""
    var email = ""
    fun getGreetingMessage(view: View){

        val greeting_message = requireActivity().findViewById<TextView>(R.id.greeting_message)

        email = requireArguments().getString("email").toString()

        db.collection("users")
            .document(email!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.toObject(User::class.java)
                    if (data != null) {
                        CurrentUser.user = data
                        userName = data.name
                        greeting_message.text = "Hello $userName!"
                    }
                }
            }
    }
    var currentUser = userName
}

