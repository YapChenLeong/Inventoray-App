package com.example.inventory.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inventory.R
import com.example.inventory.adapters.TabsViewPagerAdapter
import com.example.inventory.databinding.FragmentViewPagerTabsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator


class TabsViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerTabsBinding.inflate(inflater, container, false)

        val view = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.visibility = View.VISIBLE

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowCustomEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(R.string.app_name)
        val fragmentList = arrayListOf<Fragment>(
            ItemListFragment(),
            CalendarItemFragment()
        )

// !! Due to crash issue when rotate app using activity’s supportFragmentManager, change to use childFragmentManager
// Reason: It seems that Android somehow kills and creates the caller Fragment.
// But we have ViewModel and LiveData. Observer directly called right after Fragment called.
// While its transaction is ongoing, it tries to execute new transaction in order to show DialogFragment.
// ---------
// Solution: Instead of using fragmentManager(because it is activity’s supportFragmentManager),
// we should use childFragmentManager. In that way Android can manage to handle Fragment Transactions.
// The crash doesn’t happen again.
//        val adapter = ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        val adapter = TabsViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager2){tab, position->
            when(position){
                0 -> {
                    tab.text= "Data"
                    tab.setIcon(R.drawable.ic_baseline_show_chart_24)
                }
                1 -> {
                    tab.text= "Calendar"
                    tab.setIcon(R.drawable.ic_baseline_calendar_today_24)
                }
            }
        }.attach()


        return binding.root
    }

}

