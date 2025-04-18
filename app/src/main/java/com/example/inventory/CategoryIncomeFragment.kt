package com.example.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.inventory.data.CategorySettings
import com.example.inventory.databinding.FragmentCategoryIncomeBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import com.example.inventory.views.CategorySettingTabsFragmentDirections


class CategoryIncomeFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding : FragmentCategoryIncomeBinding? = null
    private val binding get() = _binding!!

    private var singleItem : CategorySettings? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryIncomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_category -> {
//                val message = binding.categoryName.text.toString()
                // Handle the action icon click event
                val action = CategorySettingTabsFragmentDirections.actionCategorySettingTabsFragmentToAddCategoryFragment(
                    title = getString(R.string.add_exp_category), categoryData = singleItem ?: CategorySettings("", 0, "", emptyList())

                )
                this.findNavController().navigate(action)

                true
            }
            android.R.id.home -> {
//                val action = CategorySettingTabsFragmentDirections.actionCategorySettingTabsFragmentToChooseItemFragment()
                val action = CategorySettingTabsFragmentDirections.actionCategorySettingTabsFragmentToChooseItemTabsFragment()
                this.findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        viewModel.deleteExpenseAndSubItems.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }
}