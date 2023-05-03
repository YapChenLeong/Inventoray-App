/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.views

import Common.Common
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.*
import com.example.inventory.adapters.ItemListAdapter
import com.example.inventory.adapters.SampleAdapter
import com.example.inventory.data.Header
import com.example.inventory.data.Item
import com.example.inventory.databinding.ItemListFragmentBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!
    private var newItemList: MutableList<Item> = ArrayList()
    private var newItemDateList: MutableList<Item> = ArrayList()
    private var itemList: MutableList<Item> = ArrayList()
    //to delete
//    private var adapter = SampleAdapter{
//        Log.d("asd","asdasdasd")
//        val action =
//            ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
//        this.findNavController().navigate(action)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SampleAdapter {
//            val action = TabsViewPagerFragmentDirections.actionViewPagerFragmentToItemDetailFragment(it.id)
            val action = ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id)
            this.findNavController().navigate(action)
        }

//        binding.parentRecycleView.layoutManager = GridLayoutManager(this.context,7)
        binding.parentRecycleView.layoutManager = LinearLayoutManager(this.context)
        binding.parentRecycleView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->

            if (items.isNotEmpty()){

                adapter.list = combineTransactionData(items)
                adapter.notifyDataSetChanged()

                itemList.clear()

                for(item in items){
                    itemList.add(item)
                }
                newItemDateList = Common.sortDate(itemList)
                newItemDateList = Common.combineDates(itemList)

//                newItemList = Common.sortCountry(itemList)
//                newItemList = Common.combineItems(itemList)

//                for(newItem in newItemList){
//                    items.add(newItem)
//                }
            }

            //Inside the observer, call submitList() on the adapter and pass in the new list.
            // This will update the RecyclerView with the new items on the list.
            let {
//                adapter.submitList(newItemDateList)
            }
        }

        binding.floatingActionButton.setOnClickListener {
//
            val action = TabsViewPagerFragmentDirections.actionViewPagerFragmentToAddItemFragment(
                getString(R.string.add_fragment_title)
            )
//            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
//                getString(R.string.add_fragment_title)
//            )
            this.findNavController().navigate(action)
        }
    }

    private fun combineTransactionData(items : List<Item>) : List<Any> {
        val list : MutableList<Any> = mutableListOf()
        var date : Date? = null
        for (item in items) {
             if (date == null || !isSameDay(date,item.date!!)) {
                 list.add(Header(item.date!!))
                 date = item.date
             }
            list.add(item)
        }
        return list
    }

    private fun isSameDay(date1 : Date ,date2 : Date) : Boolean{
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        var a = cal1[Calendar.MONTH] + 1

        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
    }
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}
