package com.example.mateconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mateconnect.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding



    override fun onStart() {
        super.onStart()
        toggleSignOutButton(false)
    }

    override fun onStop() {
        super.onStop()
        toggleSignOutButton(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        toggleSignOutButton(false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appToolBar)

        val navController = findNavController(R.id.my_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.messageButton.setOnClickListener {
            navController.navigate(R.id.chatListFragment)
        }
        binding.notificationButton.setOnClickListener {
            navController.navigate(R.id.notificationFragement)
        }

        binding.signOut.setOnClickListener{
            Firebase.auth.signOut()
            navController.navigate(R.id.loginFragment)
        }

        binding.bottomNavigation.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.home_page_menu_button -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.map_page_menu_button -> {

                    val intent = Intent(this, MapFragment::class.java)
                    startActivity(intent)

                }
                R.id.account_page_menu_button -> {
                    navController.navigate(R.id.accountFragment)
                }
                R.id.search_page_menu_button -> {
                    navController.navigate(R.id.searchFragment)
                }
                R.id.connections_page_menu_button -> {
                    navController.navigate(R.id.connectionsFragment)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun toggleSignOutButton(show: Boolean) {
        val signOut = findViewById<Button>(R.id.signOut)
        signOut.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
