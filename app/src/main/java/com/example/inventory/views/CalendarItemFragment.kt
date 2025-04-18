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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.InventoryApplication
import com.example.inventory.adapters.CalendarAdapter
import com.example.inventory.data.Header
import com.example.inventory.data.Item
import com.example.inventory.databinding.FragmentCalendarViewLayoutBinding
import com.example.inventory.viewModels.InventoryViewModel
import com.example.inventory.viewModels.InventoryViewModelFactory
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CalendarItemFragment : Fragment(), CalendarAdapter.OnItemListener {
    private val viewModel: InventoryViewModel by activityViewModels {
        InventoryViewModelFactory(
            (activity?.application as InventoryApplication).database.itemDao()
        )
    }

    private var selectedDate: LocalDate? = null

    private var _binding: FragmentCalendarViewLayoutBinding? = null
    private val binding get() = _binding!!
    private var daysInMonth = ArrayList<String>()
    private var transactionData : List<Any> = listOf()
    private var originalData : List<Any> = listOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentCalendarViewLayoutBinding.inflate(inflater, container, false)
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

        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            val currentDate = Calendar.getInstance()
        originalData = items
        transactionData = Common.filterDate(currentDate, items, selectedDate)

//            transactionData = combineTransactionData(items) as ArrayList<Any>
            setMonthView(false)
        }
        selectedDate = LocalDate.now()

        binding.previousMonthAction.setOnClickListener{
            previousMonthAction()
        }
        binding.nextMonthAction.setOnClickListener{
            nextMonthAction()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView(flag: Boolean) {
        if(flag){
            viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
                val currentDate = Calendar.getInstance()
                transactionData = Common.filterDate(currentDate, items, selectedDate)
            }
        }

        binding.monthYearTV.text = monthYearFromDate(selectedDate!!) //November 2022
        daysInMonth = daysInMonthArray(selectedDate!!)
        val calendarAdapter = CalendarAdapter(daysInMonth, this, transactionData, selectedDate)

//        binding.calendarRecyclerView.addItemDecoration(DividerItemDecoration(this.context, GridLayoutManager.VERTICAL))
//        binding.calendarRecyclerView.addItemDecoration(DividerItemDecoration(this.context, GridLayoutManager.HORIZONTAL))
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(this.context,7)
//        binding.calendarRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.calendarRecyclerView.adapter = calendarAdapter

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {
        //create an empty array
        val daysInMonthArray = ArrayList<String>()
        //example: 2023-01
        val yearMonth = YearMonth.from(date)
        //how many days in this month, example: 31
        val daysInMonth = yearMonth.lengthOfMonth()

        val currentMonth = date.monthValue
        val currentYear = date.year

        /**
         * withDayOfMonth (int dayOfMonth): The function returns a LocalDate based on this date with the requested day, not null.
         * https://www.geeksforgeeks.org/localdate-withdayofmonth-method-in-java-with-examples/
         */
        //example: //2023-01-01
        val firstOfMonth = selectedDate!!.withDayOfMonth(1)

        val firstDayOfMonth = selectedDate!!.withDayOfMonth(1).dayOfWeek
        /**
         * getDayOfWeek(): The function returns the day of the week and not null.
         * https://www.geeksforgeeks.org/localdate-getdayofweek-method-in-java/
         */
        //means the day number will started based on the dayweek
        //example: sunday is 7, monday is 1
        val dayOfWeek = firstOfMonth.dayOfWeek.value //2

        var gridCount = 1
        //use 42 because maxim only has 6 weeks in calendar

        /**
         * To fill the grid for previous day month
         * */
        for(i in 1..dayOfWeek){
//            daysInMonthArray.add("")
            if(i == 7){
                daysInMonthArray.clear()
                gridCount = 1
            }
            else{
                if(gridCount <= dayOfWeek){
                    val previousMonth = if (currentMonth == 1) 12 else currentMonth -1
                    val previousYear = if (previousMonth == 12) currentYear - 1 else currentYear

                    var previousDay = LocalDate.of(previousYear, previousMonth, 1)
                    val previousYearMonth = YearMonth.from(previousDay)
                    val previousTotalLengthInMonth = previousYearMonth.lengthOfMonth()

                    var newPreviousDay = LocalDate.of(previousYear, previousMonth, (previousTotalLengthInMonth - dayOfWeek) + 1)

                    while (gridCount <= dayOfWeek) {
                        daysInMonthArray.add((newPreviousDay.dayOfMonth.toString()))
                        newPreviousDay = newPreviousDay.plusDays(1)
                        gridCount++
                    }
                }

            }
        }

        /**
         * To fill the grid for current day month
         * */
        for (i in 1..daysInMonth) {
            val day = date.withDayOfMonth(i)
            if(i == 1){
                daysInMonthArray.add((day.dayOfMonth.toString() + "/" + day.monthValue))
            }else{
                daysInMonthArray.add((day.dayOfMonth.toString()))
            }
            gridCount++
        }

        /**
         *  To fill the grid for next day month
         * */
        if (gridCount < 43) {
            val nextMonth = if (currentMonth == 12) 1 else currentMonth + 1
            val nextYear = if (nextMonth == 1) currentYear + 1 else currentYear
            var day = LocalDate.of(nextYear, nextMonth, 1)
            while (gridCount < 43) {
                if(day.dayOfMonth == 1){
                    daysInMonthArray.add((day.dayOfMonth.toString() + "/" + day.monthValue))
                }else{
                    daysInMonthArray.add((day.dayOfMonth.toString()))
                }
                day = day.plusDays(1)
                gridCount++
            }
        }

//        for (i in 1..42) {
//            val day = date.withDayOfMonth(i)
//            //i represents the grids
//            //daysInMonth + dayOfWeek = meaning the plus the start day with end day
//            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
//                daysInMonthArray.add("")
////                if(daysInMonthArray.size == 7){
////                    daysInMonthArray.clear()
////                }
//            } else {
//                daysInMonthArray.add((i - dayOfWeek).toString())
//            }
//        }
        return daysInMonthArray
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    /**
     * previous button
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun previousMonthAction() {
        selectedDate = selectedDate!!.minusMonths(1)
        setMonthView(true)
    }

    /**
     * next button
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun nextMonthAction() {
        selectedDate = selectedDate!!.plusMonths(1)
        setMonthView(true)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, dayText: String?) {
        if (dayText != "") {
            val message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate!!)
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }
}