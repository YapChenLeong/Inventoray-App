package com.example.inventory.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.databinding.FragmentSubCategoryItemBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import java.util.*


class SubCategoryItemFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private val navigationArgs: SubCategoryItemFragmentArgs by navArgs()

    private var _binding : FragmentSubCategoryItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var inputDate: Date


    private var mainCategoryName : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSubCategoryItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainCategoryName = navigationArgs.mainCategoryName
        val subCategory = navigationArgs.subCategoryName

        binding.mainCategoryName.setText(mainCategoryName)
        binding.mainCategoryName.isFocusable = false
        binding.mainCategoryName.isClickable = false

        if(subCategory.isNotEmpty()){
            binding.subCategoryName.setText(subCategory)
        }
        binding.subCategoryName.requestFocus()

        binding.saveAction.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        if(binding.subCategoryName.text!!.isNotEmpty()){
            var defaultCalendar = Calendar.getInstance()
            inputDate = defaultCalendar.time
            viewModel.addNewSubCategory(
                navigationArgs.expenseId,
                binding.subCategoryName.text.toString(),
                inputDate,
                inputDate
            )
            val action = SubCategoryItemFragmentDirections.actionSubCategoryItemFragmentToExpenseSubCategoryFragment(
                navigationArgs.expenseId,mainCategoryName.toString(),""
            )
            this.findNavController().navigate(action)
        }
    }
}