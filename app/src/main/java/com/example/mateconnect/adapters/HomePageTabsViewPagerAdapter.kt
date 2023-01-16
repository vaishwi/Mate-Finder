package com.example.mateconnect.adapters
//references
//https://developer.android.com/guide/navigation/navigation-swipe-view-2
//https://medium.com/busoft/how-to-use-viewpager2-with-tablayout-in-android-eaf5b810ef7c

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mateconnect.HomeAllConnectionsFragment
import com.example.mateconnect.HomePinnedConnectionsFragment
import com.example.mateconnect.HomeSuggestedConnectionsFragment

private const val NUM_TABS = 3

class HomePageTabsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

        override fun getItemCount(): Int {
            return NUM_TABS
        }

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return HomeAllConnectionsFragment()
                1 -> return HomeSuggestedConnectionsFragment()
            }
            return HomePinnedConnectionsFragment()
        }
    }
