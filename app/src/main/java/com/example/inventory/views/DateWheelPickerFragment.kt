package com.example.inventory.views

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.inventory.R
import com.example.inventory.constant.Constant
import com.example.inventory.databinding.FragmentDateWheelPickerBinding
import com.example.inventory.viewModels.SharedDateTimeVM
import com.example.inventory.viewModels.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*


class DateWheelPickerFragment : Fragment() {

    private var _binding: FragmentDateWheelPickerBinding? = null
    private val binding get() = _binding!!
    private val sharedDateTimeVM: SharedDateTimeVM by activityViewModels()

    private val constant = Constant()  // Properly instantiate Constant

    interface OnDateSetListener {
        fun onDateSet(selectedDate: String)
    }
    interface OnMonthSelectedListener {
        fun onMonthSelected(month: String?)
    }

    var onDateSetListener: OnDateSetListener? = null
    var onMonthSelectedListener: OnMonthSelectedListener? = null

    var selectedDate: String? = null
    var selectedMonth: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDateWheelPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val behavior = BottomSheetBehavior.from(view.parent as View)

        //Configure the day picker
        binding.dayPicker.minValue = constant.minDay
        binding.dayPicker.maxValue = constant.maxDay
        val displayedValues = Array(binding.dayPicker.maxValue - binding.dayPicker.minValue + 1) { i ->
            String.format("%02d", i + binding.dayPicker.minValue)
        }
        //Configure the month picker
        binding.monthPicker.minValue = constant.minMonth
        binding.monthPicker.maxValue = constant.maxMonth
        val months = constant.getMonthsList().toTypedArray()

        //Configure the year picker
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.yearPicker.minValue = constant.minYear// or any other desired range
        binding.yearPicker.maxValue = currentYear + 10 // or any other desired range
        val years = Array( binding.yearPicker.maxValue - binding.yearPicker.minValue + 1) { i ->
            ( binding.yearPicker.minValue + i).toString()
        }

        binding.dayPicker.displayedValues = displayedValues
        customizeNumberPicker(binding.dayPicker)
        binding.monthPicker.displayedValues = months
        binding.yearPicker.displayedValues = years

//        behavior.state = BottomSheetBehavior.STATE_EXPANDED // Set your desired state

        //Disabled user direct input and only allows selection through scrolling.
        binding.dayPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.monthPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        binding.yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        // Get the current date
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        //Get the date from live Data
        val liveSelectedDate = sharedDateTimeVM.getSelectedDate()
        // Set the current date values to NumberPickers
        binding.dayPicker.value = if(liveSelectedDate != null)  liveSelectedDate.first else currentDay
        binding.monthPicker.value = if(liveSelectedDate != null) liveSelectedDate.second else currentMonth
        binding.yearPicker.value = if(liveSelectedDate != null) liveSelectedDate.third else currentYear
        //Get month name
        selectedMonth =  if(liveSelectedDate != null)  binding.monthPicker.displayedValues[liveSelectedDate.second] else binding.monthPicker.displayedValues[currentMonth]

        //Store the date time in live data
        sharedDateTimeVM.setSelectedDate(binding.dayPicker.value, binding.monthPicker.value, binding.yearPicker.value)
        sharedDateTimeVM.setSelectedMonth(selectedMonth.toString())

        //Select Day
        binding.dayPicker.setOnValueChangedListener { _, _, newDay ->
//            onDateSet(binding.dayPicker.value.toString() + selectedMonth + binding.yearPicker.value.toString())
            sharedDateTimeVM.setSelectedDate(newDay, binding.monthPicker.value, binding.yearPicker.value)
        }
        //Select Month
        binding.monthPicker.setOnValueChangedListener { _, _, newMonth ->
            val newMaxDay = getMaxDay(binding.yearPicker.value, newMonth)
            binding.dayPicker.maxValue = newMaxDay
            val selectedMonth = binding.monthPicker.displayedValues[newMonth]
            onMonthSelectedListener?.onMonthSelected(selectedMonth)
//            onDateSet(binding.dayPicker.value.toString() + selectedMonth + binding.yearPicker.value.toString())
            sharedDateTimeVM.setSelectedDate(binding.dayPicker.value, newMonth, binding.yearPicker.value)
            sharedDateTimeVM.setSelectedMonth(selectedMonth)
        }
        //Select Year
        binding.yearPicker.setOnValueChangedListener { _, _, newYear ->
//            onDateSet(binding.dayPicker.value.toString() + selectedMonth + binding.yearPicker.value.toString())
            sharedDateTimeVM.setSelectedDate(binding.dayPicker.value, binding.monthPicker.value, newYear)
        }

//        dayPicker.setOnValueChangedListener { _, _, newVal ->
//            sharedViewModel.setSelectedDate(newVal, monthPicker.value, yearPicker.value)
//        }
//        monthPicker.setOnValueChangedListener { _, _, newVal ->
//            sharedViewModel.setSelectedDate(dayPicker.value, newVal, yearPicker.value)
//        }
//        yearPicker.setOnValueChangedListener { _, _, newVal ->
//            sharedViewModel.setSelectedDate(dayPicker.value, monthPicker.value, newVal)
//        }

//        binding.buttonSet.setOnClickListener {
//            if(binding.dayPicker.value == currentDay && binding.monthPicker.value == currentMonth && binding.yearPicker.value == currentYear){
//                val df = SimpleDateFormat("dd MMM", Locale.ENGLISH)
//                selectedDate = "Today \n" + df.format(calendar.time)
//            }else{
//
//                val inputDate = binding.dayPicker.value.toString() + " " + selectedMonth + " " + binding.yearPicker.value.toString()
//                val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
//                val outputFormat = SimpleDateFormat("dd MMM", Locale.ENGLISH)
//
//                var date = inputFormat.parse(inputDate)
//                selectedDate = outputFormat.format(date)
////                selectedDate = df.format(calendar.time)
//            }
//
//            onDateSet(selectedDate.toString()+ "\n" +binding.yearPicker.value.toString() )
//        }

//        binding.buttonCancel.setOnClickListener {
//            dismiss()
//        }
    }
    private fun onDateSet(date: String) {
        onDateSetListener?.onDateSet(date)
//        dismiss() // Close the bottom sheet after setting the date
    }


    // Example function to demonstrate calling onDateSet
     fun onDateSelected() {
        val selectedDate = selectedDate
        if (selectedDate != null) {
            onDateSet(selectedDate)
        }

    }



    private fun getMaxDay(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1) // Set the day to the first day of the month
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    private fun customizeNumberPicker(dayPicker: NumberPicker) {
//        dayPicker.wrapSelectorWheel = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}