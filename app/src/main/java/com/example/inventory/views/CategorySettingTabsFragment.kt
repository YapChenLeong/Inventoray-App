package com.example.inventory.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventory.CategoryIncomeFragment
import com.example.inventory.R
import com.example.inventory.adapters.TabsViewPagerAdapter
import com.example.inventory.databinding.FragmentCategorySettingTabsBinding
import com.google.android.material.tabs.TabLayoutMediator


class CategorySettingTabsFragment : Fragment() {

    private var _binding: FragmentCategorySettingTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentCategorySettingTabsBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            CategoryExpensesFragment(),
            CategoryIncomeFragment()
        )
        val adapter = TabsViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewPager2){tab, position->
            when(position){
                0 -> {
                    tab.text= "Expenses"
//                    tab.setIcon(R.drawable.ic_baseline_show_chart_24)
                }
                1 -> {
                    tab.text= "Income"
//                    tab.setIcon(R.drawable.ic_baseline_calendar_today_24)
                }
            }
        }.attach()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



}