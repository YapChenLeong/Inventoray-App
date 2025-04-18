package com.example.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventory.adapters.TabsViewPagerAdapter
import com.example.inventory.databinding.FragmentCategoryExpensesBinding
import com.example.inventory.databinding.FragmentChooseExpenseBinding
import com.example.inventory.views.CategoryExpensesFragment
import com.google.android.material.tabs.TabLayoutMediator

class ChooseExpenseFragment : Fragment() {

    private var _binding : FragmentChooseExpenseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseExpenseBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}