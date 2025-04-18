package com.example.inventory.views

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.inventory.adapters.TabsViewPagerAdapter
import com.example.inventory.databinding.FragmentStatisticTabsBinding
import com.example.inventory.viewModels.SharedDateTimeVM
import com.example.inventory.viewModels.SharedStatisticViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.fragment.app.viewModels



class StatisticTabsFragment : Fragment() {

    //Now the ViewModel is scoped to StatisticTabsFragment only — so navigating away will destroy it, and coming back will create a new one.
    private val sharedStatisticViewModel: SharedStatisticViewModel by activityViewModels()

    private var _binding: FragmentStatisticTabsBinding? = null
    private val binding get() = _binding!!
    private lateinit var statisticFragment: StatisticFragment
    private lateinit var incomeStatisticFragment: IncomeStatisticFragment

    private var selectedDate: LocalDate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatisticTabsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticFragment = StatisticFragment()
        incomeStatisticFragment = IncomeStatisticFragment()
        selectedDate = LocalDate.now()

        val fragmentList = arrayListOf<Fragment>(
            statisticFragment,
            incomeStatisticFragment
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

        binding.statNextAction.setOnClickListener{
            when (binding.chooseItemViewpager.currentItem) {
                0 -> statisticFragment.onNextAction()
                1 -> incomeStatisticFragment.onNextAction()
            }
        }

        binding.statPreviousAction.setOnClickListener{
            when (binding.chooseItemViewpager.currentItem) {
                0 -> statisticFragment.onPreviousAction()
                1 -> incomeStatisticFragment.onPreviousAction()
            }
        }

        // Check if there's already a selected date in ViewModel
        val existingDate = sharedStatisticViewModel.selectedDate.value
        if (existingDate != null) {
            selectedDate = existingDate
        } else {
            sharedStatisticViewModel.updateDate(selectedDate!!)
        }


        binding.staMonthYearTv.text = monthYearFromDate(selectedDate!!)

        sharedStatisticViewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            binding.staMonthYearTv.text = monthYearFromDate(date)
        }


//        for(i in 0..1){
//            val textView: TextView = when (i) {
//                0 -> {
//                    LayoutInflater.from(requireContext()).inflate(R.layout.tab_expense_category, null) as TextView
//                }
//
//                1 -> {
//                    LayoutInflater.from(requireContext()).inflate(R.layout.tab_title2, null) as TextView
//                }
//
//                else -> continue
//
//            }
//            binding.chooseItemTabs.getTabAt(i)?.customView = textView
//        }

//        binding.chooseItemViewpager.isUserInputEnabled = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date?.format(formatter) ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedStatisticViewModel.updateDate(null) // or reset to LocalDate.now()
        _binding = null
    }
    // Attach listener to the parent activity or fragment


}