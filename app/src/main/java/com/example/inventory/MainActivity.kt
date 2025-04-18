
/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.inventory

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.inventory.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding.bottomAppBar.isEnabled = false
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //        supportActionBar?.hide()
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
//        // Set up the action bar for use with the NavController
        setupActionBarWithNavController(this, navController)
        bottomNavigationView.setupWithNavController(navController)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
//        setUpTabs()
    }


    private fun setUpTabs(){
//        var viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)
//        var tabslayout = findViewById<TabLayout>(R.id.tabs)
//
//        val fragmentList = arrayListOf<Fragment>(
//            ItemListFragment(),
//            CalendarItemFragment()
//        )
//        val adapter = ViewPagerAdapter(fragmentList,supportFragmentManager, lifecycle)
//        adapter.addFragment(ItemListFragment(), "Data")
//        adapter.addFragment(CalendarItemFragment(), "Calendar")
//        viewPager2.adapter=adapter
//
//        TabLayoutMediator(tabslayout, viewPager2){tab, position->
//            when(position){
//                0 -> {
//                    tab.text= "Data"
//                    tab.setIcon(R.drawable.ic_baseline_show_chart_24)
//                }
//                1 -> {
//                    tab.text= "Calendar"
//                    tab.setIcon(R.drawable.ic_baseline_calendar_today_24)
//                }
//            }
//        }.attach()


//        tabs.setupWithViewPager(viewPager)
//        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_show_chart_24)
//        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_calendar_today_24)
    }

    /**
     * Handle navigation when the user chooses Up from the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
