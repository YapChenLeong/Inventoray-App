package com.example.inventory.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.inventory.R
import com.example.inventory.ChooseExpenseFragment
import com.example.inventory.adapters.TabsViewPagerAdapter
import com.example.inventory.databinding.FragmentChooseItemTabsBinding
import com.google.android.material.tabs.TabLayoutMediator


class ChooseItemTabsFragment : Fragment() {

    private var _binding: FragmentChooseItemTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChooseItemTabsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = arrayListOf<Fragment>(
            ChooseItemFragment(),
            ChooseIncomeFragment()
        )
        val adapter = TabsViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)

        binding.chooseItemViewpager.adapter = adapter
        TabLayoutMediator(binding.chooseItemTabs, binding.chooseItemViewpager){tab, position->
            when(position){
                0 -> {
                    tab.text= "Expenses"
                }
                1 -> {
                    tab.text= "Income"
                }
            }
        }.attach()

        for(i in 0..1){
            val textView: TextView = when (i) {
                0 -> {
                    LayoutInflater.from(requireContext()).inflate(R.layout.tab_expense_category, null) as TextView
                }

                1 -> {
                    LayoutInflater.from(requireContext()).inflate(R.layout.tab_title2, null) as TextView
                }

                else -> continue

            }
            binding.chooseItemTabs.getTabAt(i)?.customView = textView
        }
//        TabLayoutMediator(binding.chooseItemTabs, binding.chooseItemViewpager){ tab, position->
//            tab.customView = LayoutInflater.from(context).inflate(R.layout.custom_tab_button, null).apply {
//                (this as TextView).text = when (position) {
//                    0 -> "Expenses"
//                    1 -> "Income"
//                    else -> ""
//                }
//            }
//        }.attach()

        binding.chooseItemViewpager.isUserInputEnabled = false

    }

}