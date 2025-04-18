package com.example.inventory.views

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.DragStartHelper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.InventoryApplication
import com.example.inventory.R
import com.example.inventory.adapters.ItemTouchHelperCallback
import com.example.inventory.adapters.SubCategoryAdapter
import com.example.inventory.data.CategoryLists
import com.example.inventory.data.ItemTouchHelperAdapter
import com.example.inventory.data.SubCategoryData
import com.example.inventory.data.SubCategoryLists
import com.example.inventory.databinding.FragmentExpenseSubCategoryBinding
import com.example.inventory.databinding.FragmentSubCategoryBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory


class ExpenseSubCategoryFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private val navigationArgs: ExpenseSubCategoryFragmentArgs by navArgs()

    private var _binding : FragmentExpenseSubCategoryBinding? = null
    private val binding get() = _binding!!

    private var itemTouchHelper : ItemTouchHelper? = null
    private lateinit var adapter: SubCategoryAdapter // Change to singular 'adapter'

    private var mainCategoryName : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseSubCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

//        val groupData = navigationArgs.subListData
//        val itemList = groupData?.items

        val expenseId = navigationArgs.expenseId

        mainCategoryName = navigationArgs.title

        viewModel.getSubCategoryData(expenseId).observe(this.viewLifecycleOwner){  allItems ->
            setupRecyclerView(allItems as List<SubCategoryData>)
        }

        // Observe deleteSuccess LiveData
        viewModel.deleteSuccess.observe(this.viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                // Handle successful deletion
//                    viewModel.fetchSubCategoryData(expenseId)
                Toast.makeText(requireContext(), "Item deleted successfully", Toast.LENGTH_SHORT).show()
                // You may also refresh your data or update the UI here
            } else {
                // Handle deletion failure
                Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_list_menu -> {
            // Handle the action icon click event

//                Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                val action = ExpenseSubCategoryFragmentDirections.actionExpenseSubCategoryFragmentToSubCategoryItemFragment(
                    navigationArgs.expenseId,getString(R.string.add_subcategory_title), mainCategoryName.toString() ,""
                )
                this.findNavController().navigate(action)

                true
            }
            android.R.id.home -> {
                val action = ExpenseSubCategoryFragmentDirections.actionExpenseSubCategoryFragmentToCategorySettingTabsFragment(
                    getString(R.string.adjust_category)
                )
                this.findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun showConfirmationDialog(removeItemClicked: SubCategoryData) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_confirmation_dialog_layout, null)

        val builder = android.app.AlertDialog.Builder(requireContext())
        val dialog = builder.setView(dialogView).create()

        dialogView.findViewById<TextView>(R.id.btnDelete).setOnClickListener {
            // Handle positive button click (user confirmed)
            // Implement your action here
            if(removeItemClicked.id.isNotEmpty() && removeItemClicked.id.isNotBlank()){
                viewModel.deleteSubCategoryData(removeItemClicked.id)
            }
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            // Handle negative button click (user canceled)
            dialog.dismiss()
        }

        dialog.show()
//        val builder = android.app.AlertDialog.Builder(requireContext())
//
//        builder.setTitle("Confirmation")
//            .setMessage("Are you sure you want to perform this action?")
//            .setPositiveButton("Delete") { dialogInterface: DialogInterface, _: Int ->
//                // Handle positive button click (user confirmed)
//                // Implement your action here
//                dialogInterface.dismiss()
//            }
//            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
//                // Handle negative button click (user canceled)
//                dialogInterface.dismiss()
//            }
//            .show()
    }

    private fun setupRecyclerView(items: List<SubCategoryData>) {
        adapter = SubCategoryAdapter(
            items,
            requireActivity(),
            onIconClicked = { itemClicked ->
                val action = ExpenseSubCategoryFragmentDirections.actionExpenseSubCategoryFragmentToSubCategoryItemFragment(
                    navigationArgs.expenseId,getString(R.string.modify_subcategory),mainCategoryName.toString(),itemClicked.description01!!
                )
                this.findNavController().navigate(action)
            },
            onRemoveClicked = { removeItemClicked ->
                showConfirmationDialog(removeItemClicked)
                // Handle remove click
                // The 'itemId' parameter contains the ID or identifier of the item to be removed
                // Implement your logic here
            },
            onDragClicked = {viewHolder ->
                itemTouchHelper?.startDrag(viewHolder)
            }
        )
        binding.subcategoryRecycleView.layoutManager = LinearLayoutManager(context)
        binding.subcategoryRecycleView.adapter = adapter

        val subCategoryTouchHelperAdapter = adapter as ItemTouchHelperAdapter

        // Create an instance of ItemTouchHelperCallback
        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)

        // Create an instance of ItemTouchHelper using the callback
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

        // Set the ItemTouchHelper to the RecyclerView
        itemTouchHelper?.attachToRecyclerView(binding.subcategoryRecycleView)
    }

}