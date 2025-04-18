package com.example.inventory.views

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.adapters.CategorySettingAdapter
import com.example.inventory.adapters.ItemTouchHelperCallback
import com.example.inventory.adapters.ItemTouchHelperCallback2
import com.example.inventory.adapters.SubCategoryAdapter
import com.example.inventory.data.*
import com.example.inventory.databinding.FragmentCategoryExpensesBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory


class CategoryExpensesFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }
    private var _binding : FragmentCategoryExpensesBinding? = null
    private val binding get() = _binding!!

    private var itemTouchHelper : ItemTouchHelper? = null
    private lateinit var adapter: CategorySettingAdapter // Change to singular 'adapter'
    private var pExpenseId : String? = null

    private var allExpenseList: List<ExpenseData> = emptyList()
    private var isDelete : Boolean = false

    private var isUpdatingFromDeletion = false

    private var singleItem : CategorySettings? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryExpensesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.getExpenseData.observe(this.viewLifecycleOwner){ expensesCategory ->
            setupRecyclerView(expensesCategory as List<ExpenseDataWithSubExpense> )
        }

        // Observe deleteSuccess LiveData
        viewModel.deleteExpenseAndSubItems.observe(this.viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show()

                if(!isUpdatingFromDeletion){

                    viewModel.getAllExpenseData().observe(this.viewLifecycleOwner) { expensesCategory ->
                        if (!isUpdatingFromDeletion) {
                            allExpenseList = expensesCategory
                            viewModel.updateExpenseListIndex(expensesCategory)
                        }
                        isUpdatingFromDeletion = true
                    }
                }

                // You may also refresh your data or update the UI here
            } else {
                // Handle deletion failure
                Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun restructureData(items: List<ExpenseDataWithSubExpense>): List<Any>{
        var list : MutableList<CategorySettings> = mutableListOf()

        var headList : MutableList<Any> = mutableListOf()
        var subList : MutableList<SubCategoryLists> = mutableListOf()

        var headId : String? = null

        for(item in items){
            if((headId == null || headId != item.expenseId)){
                if(subList.isNotEmpty() && subList.size > 0){
                    if(list.isNotEmpty()){
                        // Create a new list to avoid passing the reference
                        val newList = ArrayList(subList)
                        headList.add(CategorySettings(list[0].expenseId, list[0].imageResourceId, list[0].title, newList))
                    }
                    list.clear()
                    subList.clear()
                }

                if(item.subExpenseId.isNullOrEmpty()){
                    headList.add(CategorySettings(item.expenseId, item.imageResourceId, item.expenseName, subList))
                }else{
                    list.add(CategorySettings(item.expenseId, item.imageResourceId, item.expenseName, subList))
                    subList.add(SubCategoryLists(item.expenseId,item.subExpenseId,item.subExpenseName))
                }

                headId = item.expenseId

            }else{ //still equal to same head ID
                if(item.subExpenseId!!.isNotBlank() && item.expenseId == headId){
                    subList.add(SubCategoryLists(item.expenseId,item.subExpenseId,item.subExpenseName))
                }
            }
        }

        return headList
    }

    private fun showConfirmationDialog(removeItemClicked: CategorySettings) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_confirmation_dialog_layout, null)

        val builder = android.app.AlertDialog.Builder(requireContext())
        val dialog = builder.setView(dialogView).create()

        dialogView.findViewById<TextView>(R.id.btnDelete).setOnClickListener {
            // Handle positive button click (user confirmed)
            // Implement your action here'
            if(removeItemClicked.expenseId.isNotEmpty() && removeItemClicked.expenseId.isNotBlank()){
                isUpdatingFromDeletion = false
                viewModel.deleteExpenseAndSubItems(removeItemClicked.expenseId)
            }
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            // Handle negative button click (user canceled)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupRecyclerView(expensesCategory: List<ExpenseDataWithSubExpense>) {
            var groupCategory = restructureData(expensesCategory)
            adapter = CategorySettingAdapter(
                groupCategory as List<CategorySettings>,
                requireActivity(),
                onRemoveClicked = {removeClicked ->
                    showConfirmationDialog(removeClicked)
                },
                onItemClicked = {  onItemClicked, secondParam ->
                    if(secondParam == "imageClicked"){
                        val itemListData = SubCategoryListData(onItemClicked.mList!!)
                        val action = CategorySettingTabsFragmentDirections.actionCategorySettingTabsFragmentToAddCategoryFragment(
                            onItemClicked.title, onItemClicked
                        )
                        this.findNavController().navigate(action)
                    }else{
                        val itemListData = SubCategoryListData(onItemClicked.mList)
                        val action = CategorySettingTabsFragmentDirections.actionCategorySettingTabsFragmentToExpenseSubCategoryFragment(
                            onItemClicked.expenseId,onItemClicked.title,secondParam.toString()
                        )
                        this.findNavController().navigate(action)

                    }
                },
                onDragClicked = {viewHolder, expenseId ->
                    pExpenseId = expenseId
                    itemTouchHelper?.startDrag(viewHolder)
                }
            )
            binding.expensesRecycleView.layoutManager = LinearLayoutManager(this.context)
            binding.expensesRecycleView.adapter = adapter

            val categoryTouchHelperAdapter = adapter as ItemTouchHelperAdapter

            // Create an instance of ItemTouchHelperCallback
            val itemTouchHelperCallback = ItemTouchHelperCallback2(adapter, viewModel, pExpenseId)

            // Create an instance of ItemTouchHelper using the callback
            itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

            // Set the ItemTouchHelper to the RecyclerView
            itemTouchHelper?.attachToRecyclerView(binding.expensesRecycleView)
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

