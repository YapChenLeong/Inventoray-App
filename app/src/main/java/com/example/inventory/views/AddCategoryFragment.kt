package com.example.inventory.views

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.adapters.CategoryChildAdapter
import com.example.inventory.adapters.CategoryNameParentAdapter
import com.example.inventory.adapters.CountryAdapter
import com.example.inventory.adapters.ItemListData
import com.example.inventory.data.CategoryResources
import com.example.inventory.data.DataResources
import com.example.inventory.data.ExpenseData
import com.example.inventory.data.ExpenseItems
import com.example.inventory.databinding.FragmentAddCategoryBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import java.util.*


class AddCategoryFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }
    private val navigationArgs: AddCategoryFragmentArgs by navArgs()

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!
    val dummyCategoryData = CategoryResources().loadCategoryData()

    private val dataResources = DataResources()
    private val expenseItemsList = dataResources.loadExpensesData().toMutableList()

    private var allExpenseList: List<ExpenseData> = emptyList()

    @DrawableRes
    var imageResourceId : Int? = null

    @DrawableRes
    var selectedImageResourceId : Int? = null

    private lateinit var inputDate: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if(navigationArgs.categoryData.title.isNotEmpty() || navigationArgs.categoryData.title.isNotBlank()){
            binding.categoryName.setText(navigationArgs.categoryData.title)
        }

        val categoryAdapter = CategoryNameParentAdapter(dummyCategoryData){ iconIdClicked ->
            imageResourceId = iconIdClicked
            binding.selectedItemImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), iconIdClicked))
        }
        viewModel.getAllExpenseData().observe(this.viewLifecycleOwner) { expensesCategory ->
             allExpenseList = expensesCategory
        }

        binding.gridCategoryItemRecycleView.layoutManager = LinearLayoutManager(this.context)
        binding.gridCategoryItemRecycleView.adapter = categoryAdapter
        binding.gridCategoryItemRecycleView.setHasFixedSize(true)
    }

    private fun addNewCategory() {
        if(isEntryValid()){
            var defaultCalendar = Calendar.getInstance()
            inputDate = defaultCalendar.time
            if(imageResourceId == null){ //when default
                binding.selectedItemImage.setImageResource(R.drawable.food)
                imageResourceId = R.drawable.food
            }else{
                binding.selectedItemImage.setImageResource(imageResourceId!!)
            }

            selectedImageResourceId = binding.selectedItemImage.id
            viewModel.addNewCategory(
                imageResourceId!!,
                "Expense",
                 binding.categoryName.text.toString(),
                 inputDate,
                 inputDate,
                allExpenseList.size + 1
            )
        }else{
        }   //
    }
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid2(
            binding.categoryName.toString()
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.done_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_done -> {
                val message = binding.categoryName.text.toString()
                // Handle the action icon click event

                addNewCategory()

                Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
//                val action = AddCategoryFragmentDirections.actionAddCategoryFragmentToChooseItemFragment()
                val action = AddCategoryFragmentDirections.actionAddCategoryFragmentToChooseItemTabsFragment()
                this.findNavController().navigate(action)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}