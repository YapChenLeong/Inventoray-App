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
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.*
import com.example.inventory.adapters.CalendarAdapter
import com.example.inventory.adapters.SampleAdapter
import com.example.inventory.adapters.TransactionsAdapter
import com.example.inventory.data.*
import com.example.inventory.databinding.ItemListFragmentBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import com.example.inventory.viewModels.TransactionViewModel
import com.example.inventory.viewModels.TransactionViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

/**
 * Main fragment displaying details for all items in the database.
 */
class ItemListFragment : Fragment() {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private val transactionViewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var _binding: ItemListFragmentBinding? = null
    private val binding get() = _binding!!
    private var newItemList: MutableList<Item> = ArrayList()
    private var newItemDateList: MutableList<Item> = ArrayList()
    private var itemList: MutableList<Item> = ArrayList()
    private var transactionData : List<Any> = listOf()
    private var selectedDate: LocalDate? = null
    private var sumTotalExpense = "";
    private var sumTotalIncome = "";

    private lateinit var adapter: TransactionsAdapter


    val defaultExpenseData = SkeletonData().insertExpensesData()
    val defaultIncomeData = SkeletonData().insertIncomeData()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.supportActionBar?.apply {
            show()
            setDisplayShowCustomEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(true)
        }

        viewModel.getSettingData.observe(this.viewLifecycleOwner) { settingData ->
            if (settingData.isEmpty()) {
//                viewModel.insertionStatus.observe(viewLifecycleOwner) { isSuccess ->
//                    if (isSuccess) {
////                      val defaultSetting = SettingData(defaultValue = "Y")
//                        val defaultSetting = SettingData(defaultValue = "Y", dt_create = Common.generateCurrentDateTime(), dt_update = Common.generateCurrentDateTime())
//                        viewModel.insertSettingData(defaultSetting)
//                        GlobalVariables.defaultCategoryItems = "Y"
//                    } else {
//                        Toast.makeText(requireContext(), "Insert Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
                viewModel.autoInsertExpenseData(defaultExpenseData)
                viewModel.autoInsertIncomeData(defaultIncomeData)
                val defaultSetting = SettingData(defaultValue = "Y", dt_create = Common.generateCurrentDateTime(), dt_update = Common.generateCurrentDateTime())
                viewModel.insertSettingData(defaultSetting)
                GlobalVariables.defaultCategoryItems = "Y"
            }else {
                GlobalVariables.defaultCategoryItems = settingData[0].defaultValue
            }
        }


//        val getSettingInformation = viewModel.getSettingData()
//        if(getSettingInformation == null || getSettingInformation == undefined){
//            val defaultSetting = SettingData(defaultValue = "Y")
//            viewModel.insertSettingData(defaultSetting)
//        }

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//        val bar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
//        val bottomButton = requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.background = null
//        bottomNavigationView.menu.getItem(2).isEnabled = false //disabled menu bottom
//        bar.visibility = View.VISIBLE
//        bottomButton.visibility = View.VISIBLE

        adapter = TransactionsAdapter {
//        val adapter = SampleAdapter {
            val action = TabsViewPagerFragmentDirections.actionViewPagerFragmentToItemDetailFragment(it.id)//nav graph
//            val action = ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(it.id) //not nab graph
            this.findNavController().navigate(action)
        }

//      binding.parentRecycleView.layoutManager = GridLayoutManager(this.context,7)
        binding.parentRecycleView.layoutManager = LinearLayoutManager(this.context)
        binding.parentRecycleView.adapter = adapter
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.
        selectedDate = LocalDate.now()

        transactionViewModel.getAllTransactionData.observe(this.viewLifecycleOwner) { items ->
//        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            // items = arrayList [ {}, {}, {} ]
            getTransactionData(items)

            //Set the current date
//            var defaultCalendar = Calendar.getInstance()
//            val df = SimpleDateFormat("MMM yyyy", Locale.getDefault())
//            binding.monthYearTV.text = df.format(defaultCalendar.time)

            //Inside the observer, call submitList() on the adapter and pass in the new list.
            // This will update the RecyclerView with the new items on the list.
            let {
//                adapter.submitList(newItemDateList)
            }
        }

//        bottomButton.setOnClickListener {
//            val action = TabsViewPagerFragmentDirections.actionTabsViewPagerFragmentToChooseItemFragment()
//
//            val action = TabsViewPagerFragmentDirections.actionViewPagerFragmentToAddItemFragment(
//                getString(R.string.add_fragment_title)
//            )
//            val action = ItemListFragmentDirections.actionItemListFragmentToAddItemFragment(
//                getString(R.string.add_fragment_title)
//            )
//            this.findNavController().navigate(action)
//        }

        binding.previousMonthAction.setOnClickListener{
            previousMonthAction()
        }
        binding.nextMonthAction.setOnClickListener{
            nextMonthAction()
        }
    }

    private fun getTotalAmount(items: List<TransactionListData>, type: String): String {
        val result: List<TransactionListData> = when (type){
            "E" -> items.filter { it.type == "Expense" }
            "I" -> items.filter { it.type == "Income" }
            else -> emptyList()
        }
        val totalPrice = result.sumOf { it.amountValue.toDoubleOrNull()?: 0.0 }

        // Determine max decimal places present in the list
        val maxDecimalPlaces = result.maxOfOrNull {
            it.amountValue.toBigDecimalOrNull()?.scale() ?: 0
        } ?: 0

        // Adjust decimal format dynamically
        val decimalFormat = when {
            maxDecimalPlaces == 0 -> DecimalFormat("#")
            maxDecimalPlaces == 1 -> DecimalFormat("#.0")
            else -> DecimalFormat("#.${"0".repeat(maxDecimalPlaces)}") // Ensures dynamic precision
        }

        val formattedPrice = decimalFormat.format(totalPrice)
        return (formattedPrice ?: 0).toString()
    }

    private fun declareDecimalValue(amountValue: String): String{
        val transactionAmount = amountValue.toDoubleOrNull()?: 0.0

        val maxDecimalPlaces = amountValue.toBigDecimalOrNull()?.scale() ?: 0

        // Adjust decimal format dynamically
        val decimalFormat = when {
            maxDecimalPlaces == 0 -> DecimalFormat("#")
            maxDecimalPlaces == 1 -> DecimalFormat("#.0")
            else -> DecimalFormat("#.${"0".repeat(maxDecimalPlaces)}") // Ensures dynamic precision
        }
        val formattedPrice = decimalFormat.format(transactionAmount)
        return (formattedPrice ?: 0).toString()
    }

   private fun formatAmount(value: Double): String {
        return if (value % 1.0 == 0.0) {
            // No decimal part
            value.toInt().toString()
        } else if (value * 10 % 1.0 == 0.0) {
            // One decimal place
            String.format("%.1f", value)
        } else {
            // Two decimal places
            String.format("%.2f", value)
        }
    }

    private fun getTransactionEmptyItem(items: List<TransactionListData>?): List<Any> {
        val list : MutableList<Any> = mutableListOf()
        list.add(EmptyHeader())

        return list
    }

    private fun getEmptyItem(items: List<Item>?): List<Any> {
        val list : MutableList<Any> = mutableListOf()
        list.add(EmptyHeader())

        return list
    }

    // Helper data class for the summary
    data class DailySummary(val expenseTotal: Double, val incomeTotal: Double)

    private fun combineTransactionData2(items : List<TransactionListData>, groupDate: List<Pair<Date, List<TransactionListData>>>) : List<Any> {
        val list : MutableList<Any> = mutableListOf()
        var previousItemDate : Date? = null
        var i : Int = 0
        var totalExpenseDouble = 0.0
        var totalIncomeDouble = 0.0
        var sumExpenseDaily = getTotalAmount(transactionData as List<TransactionListData>, "E")
        var sumIncomeDaily = getTotalAmount(transactionData as List<TransactionListData>, "I")

        // Group by date first
        val groupedByDate = items.groupBy { transaction ->
            // Normalize the date by stripping time information
            Calendar.getInstance().apply {
                time = transaction.dtCreate
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time
        }

        //Loop By Day
        groupedByDate.forEach{ (date, transactions) ->
            val expenseTotal = transactions.filter { it.type == "Expense" }.sumOf { it.amountValue.toDouble() }
            val incomeTotal = transactions.filter { it.type == "Income" }.sumOf { it.amountValue.toDouble() }

            //Add header every day transaction
            list.add(Header(date, formatAmount(incomeTotal), formatAmount(expenseTotal)))

            for(transaction in transactions){
                val formattedTransaction = transaction.copy(amountValue = formatAmount(transaction.amountValue.toDouble()))
                // Create a new TransactionListData object with formatted amount
                list.add(formattedTransaction)
            }
        }

//        groupedByDate.entries.sortedByDescending { it.key }.forEach { (date, transactions) ->
//            // Add date header
//            result.add(date)
//
//            // Add all transactions for this date
//            result.addAll(transactions.sortedByDescending { it.dtCreate })
//
//            // Calculate and add summary
//            val expenseTotal = transactions.filter { it.type == "Expense" }.sumOf { it.amountValue.toDouble() }
//            val incomeTotal = transactions.filter { it.type == "Income" }.sumOf { it.amountValue.toDouble() }
//
//            result.add(DailySummary(expenseTotal, incomeTotal))
//        }
//
//        return result

//        for (item in items) {
//            if (previousItemDate == null || !isSameDay(previousItemDate,item.dtUpdate!!)) {
//                if(item.type=="Expense")totalExpenseDouble += item.amountValue.toDouble()
//                if(item.type=="Income")totalIncomeDouble += item.amountValue.toDouble()
//                var price = getTransactionTotalPrice(groupDate, i)
//                list.add(Header(item.dtUpdate!!, "RM$sumIncomeDaily", "RM$sumExpenseDaily"))
//                i++
//                previousItemDate = item.dtUpdate
//            }
//
//            list.add(item)
//        }
        return list
    }

    private fun combineTransactionData(items : List<Item>, groupDate: List<Pair<Date, List<Item>>>) : List<Any> {
        val list : MutableList<Any> = mutableListOf()
        var previousItemDate : Date? = null
        var i : Int = 0
        var totalPurchasedPrice = 0.0 //define the variable with value
        var totalChangesPrice = 0.0
        for (item in items) {
             if (previousItemDate == null || !isSameDay(previousItemDate,item.date!!)) {
                 var price = getTotalPrice(groupDate, i)
                 list.add(Header(item.date!!, "", price))
                 i++
                 previousItemDate = item.date
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

    private fun transactionGroupItem(items: List<TransactionListData>): Map<Date, List<TransactionListData>> {
        return items.groupBy { it.dtUpdate?.stripTime()!! }
    }

    private fun groupItem(items: List<Item>): Map<Date, List<Item>> {
        return items.groupBy { it.date?.stripTime()!! }
    }

    private fun getTransactionTotalPrice(groupDate: List<Pair<Date, List<TransactionListData>>>, position: Int): String{
        val (date, items) = groupDate[position]

        return calculateTransactionTotalPrice(items, "RM")
    }

    private fun getTotalPrice(groupDate: List<Pair<Date, List<Item>>>, position: Int): String{
        val (date, items) = groupDate[position]

         return calculateTotalPrice(items, "RM")
    }

    private fun calculateTransactionTotalPrice(items: List<TransactionListData>, currencySymbol: String): String {
        val totalPrice = items.sumByDouble {
            it.amountValue.toDoubleOrNull()?: 0.0
        }
        val decimalFormat = DecimalFormat("#.00")
        val formattedPrice = decimalFormat.format(totalPrice)
        return "$currencySymbol$formattedPrice"
    }

    private fun calculateTotalPrice(items: List<Item>, currencySymbol: String): String {
        val totalPrice = items.sumByDouble { it.itemPrice }
        val decimalFormat = DecimalFormat("#.00")
        val formattedPrice = decimalFormat.format(totalPrice)
        return "$currencySymbol$formattedPrice"
    }
    private fun Date?.stripTime(): Date? {
        if (this == null) return null
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.parse(sdf.format(this))
    }
    /**
     * previous button
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun previousMonthAction() {
        selectedDate = selectedDate!!.minusMonths(1)
        setMonthView(true)
    }

    /**
     * next button
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun nextMonthAction() {
        selectedDate = selectedDate!!.plusMonths(1)
        setMonthView(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView(flag: Boolean) {
        if(flag){
            transactionViewModel.getAllTransactionData.observe(this.viewLifecycleOwner) { items ->
                getTransactionData(items)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTransactionData(items: List<TransactionListData>) {
        val currentDate = Calendar.getInstance()
        transactionData = Common.filterTransactionDate(currentDate, items, selectedDate)
        if (transactionData.isNotEmpty()) {
            //group item in same date = LinkedHasMap {value(items), key(date)}
            var groupItems = transactionGroupItem(transactionData as List<TransactionListData>)
            sumTotalExpense = getTotalAmount(transactionData as List<TransactionListData>, "E")
            sumTotalIncome = getTotalAmount(transactionData as List<TransactionListData>,"I")

            var combineTrans = combineTransactionData2(transactionData as List<TransactionListData>, groupItems.toList())
            adapter.list = combineTrans
            adapter.notifyDataSetChanged()

        }else {
            sumTotalExpense = "0"
            sumTotalIncome = "0"
            adapter.list = getTransactionEmptyItem(transactionData as List<TransactionListData>)
            adapter.notifyDataSetChanged()
        }

        var calculateBalance = sumTotalIncome.toDouble() - sumTotalExpense.toDouble()
        binding.textBalanceTV.text = if (calculateBalance > 0) {
            "RM ${String.format(calculateBalance.toString())}"
        } else if (calculateBalance % 1 == 0.0) {
            "RM ${calculateBalance.toInt()}"
        } else {
            "- RM ${String.format(abs(calculateBalance).toString())}"
        }
        binding.textEarn.text = ("RM$sumTotalIncome") ?: "RM+0"
        binding.textDeduct.text = ("RM$sumTotalExpense") ?: "RM+0"
        binding.monthYearTV.text = monthYearFromDate(selectedDate!!)
    }


    //    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}
