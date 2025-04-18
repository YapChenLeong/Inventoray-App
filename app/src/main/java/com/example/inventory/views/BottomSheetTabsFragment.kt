package com.example.inventory.views

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.*
import com.example.inventory.adapters.TabsViewPagerAdapter
import com.example.inventory.databinding.FragmentBottomSheetTabsBinding
import com.example.inventory.views.CalendarItemFragment
import com.example.inventory.views.DateWheelPickerFragment
import com.example.inventory.views.ItemListFragment
import com.google.android.material.tabs.TabLayoutMediator
import androidx.lifecycle.ViewModelProvider
import com.example.inventory.R
import com.example.inventory.adapters.BottomSheetTabsAdapter
import com.example.inventory.viewModels.SharedDateTimeVM
import com.example.inventory.viewModels.SharedViewModel
import com.example.inventory.views.TimeWheelPickerFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*


class BottomSheetTabsFragment : BottomSheetDialogFragment(), DateWheelPickerFragment.OnDateSetListener, DateWheelPickerFragment.OnMonthSelectedListener{

    private var _binding: FragmentBottomSheetTabsBinding? = null
    private val binding get() = _binding!!
    private val sharedDateTimeVM: SharedDateTimeVM by activityViewModels()

    private var selectedDatePicker: String = ""
    var selectedDate: String? = null
    var selectedMonth: String? = null
    //Define Day,Month,Year
    private var sharedDayVM: Int? = null
    private var sharedMonthVM: Int? = null
    private var sharedYearVM: Int? = null
    //Define Hour,Minute,AMPM
    private var sharedHourVM: Int? = null
    private var sharedMinuteVM: Int? = null
    private var sharedAmPmVM: Int? = null
    var selectedTime: String? = null
    var selectedMonthString: String? = null

    var selectedDates: Calendar? = null
    var selectedTimes: Calendar? = null

    interface OnBottomSheetDateSetListener {
        fun onDateTimeSet(selectedDate: String, selectedDateTime: Date)
    }
     var onBottomSheetSetListener: OnBottomSheetDateSetListener? = null


    companion object {
        const val TAG = "BottomSheetTabsFragment" // Define a TAG constant

        fun show(activity: FragmentActivity) {
            val fragment = BottomSheetTabsFragment()
            fragment.show(activity.supportFragmentManager, TAG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetTabsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf<Fragment>(
            DateWheelPickerFragment(),
            TimeWheelPickerFragment()
        )

        val adapter = BottomSheetTabsAdapter(fragmentList, childFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.bottomTabs, binding.viewPager2){tab, position->
            when(position){
                0 -> {
                    tab.text= "Date"
                }
                1 -> {
                    tab.text= "Time"
                }
            }
        }.attach()

        for(i in 0..2){
            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            binding.bottomTabs.getTabAt(i)?.customView = textView
        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        sharedDateTimeVM.selectedDate.observe(this.viewLifecycleOwner) { date ->
            sharedDayVM = date.first
            sharedMonthVM = date.second
            sharedYearVM = date.third
        }


        val datePickerBottomSheet = DateWheelPickerFragment()
        datePickerBottomSheet.onDateSetListener = this
        datePickerBottomSheet.onMonthSelectedListener = this

        binding.buttonSet.setOnClickListener {
            //Get the selected month
            sharedDateTimeVM.selectedMonth.observe(this.viewLifecycleOwner) { month ->
                selectedMonthString = month
            }
            //Get the selected time
            sharedDateTimeVM.selectedTime.observe(this.viewLifecycleOwner) { time ->
                sharedHourVM = time.first
                sharedMinuteVM = time.second
                sharedAmPmVM = time.third
            }
            //Get the selected date
            sharedDateTimeVM.selectedDate.observe(this.viewLifecycleOwner) { date ->
                sharedDayVM = date.first
                sharedMonthVM = date.second
                sharedYearVM = date.third

                //Define the current date
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH)
                val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
                val currentYear = calendar.get(Calendar.YEAR)


                if(sharedHourVM == null && sharedMinuteVM == null && sharedAmPmVM == null){
                    val currentTime = Calendar.getInstance()
                    sharedHourVM = currentTime.get(Calendar.HOUR)
                    Log.d("Hour: ", "$sharedHourVM")
                    sharedMinuteVM = currentTime.get(Calendar.MINUTE)
                      Log.d("Minute: ", "$sharedMinuteVM")
                    val isPm = currentTime.get(Calendar.AM_PM) == Calendar.PM
                    sharedAmPmVM = sharedAmPmVM ?: if (isPm) 1 else 0
                    Log.d("AM/PM: ", "$sharedAmPmVM")
                }

                //Adjust hour based on AM PM
                sharedHourVM = if((sharedAmPmVM == 1) && sharedHourVM != 12){
                    sharedHourVM!! + 12
                } else{
                    sharedHourVM
                }

                //Combine Date & Time
                val combinedCalendar = Calendar.getInstance()
                Log.d("HH:MM", "$sharedHourVM $sharedMinuteVM")//17 19
                combinedCalendar.set(sharedYearVM!!, sharedMonthVM!!, sharedDayVM!!, sharedHourVM!!, sharedMinuteVM!!)
                val timestamp = combinedCalendar.timeInMillis
                val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
                val formattedDate = sdf.format(Date(timestamp))
                Log.d("formattedDate", "$formattedDate")//20 Feb 2026 17:19:43

                //Verify selected date is equal/same to current date
                if(sharedDayVM == currentDay && sharedMonthVM == currentMonth && sharedYearVM == currentYear){
                    val df = SimpleDateFormat("dd MMM", Locale.ENGLISH)
                    selectedDate = "Today \n" + df.format(calendar.time)
                    onDateTimeSet(selectedDate.toString(), combinedCalendar.time) //update date to text button UI
                }else{
                    //20 February 2026
                    val inputDate = sharedDayVM.toString() + " " + selectedMonthString + " " + sharedYearVM.toString()
                    val inputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
                    val outputDayAndMonthFormat = SimpleDateFormat("dd MMM", Locale.ENGLISH)

                    //Fri Feb 20 00:00:00 GMT+08:00 2026
                    var date = inputFormat.parse(inputDate)
                    selectedDate = outputDayAndMonthFormat.format(date)
                    onDateTimeSet(selectedDate.toString()+ "\n" +sharedYearVM.toString(), combinedCalendar.time) //update date to text button UI
                }
                dismiss()

//            if (amPm.value == 0){
//                val selectedTime = hours.value.toString() + minutes.displayedValues[minutes.value]
//            }else{
//                val selectedTime = (hours.value + 12).toString() + minutes.displayedValues[minutes.value]
//            }
            }
        }
    }

    private fun onDateTimeSet(date: String, dateTime: Date) {
        onBottomSheetSetListener?.onDateTimeSet(date, dateTime)
        dismiss() // Close the bottom sheet after setting the date
    }

    override fun onMonthSelected(month: String?) {
        selectedMonth = month
        Log.d("SelectedMonth", "Selected month: $month")
    }

    override fun onDateSet(selectedDate: String) {
        binding.buttonSet.text = selectedDate
        selectedDatePicker = binding.buttonSet.text.toString()
    }
//    fun setOnDateSetListener(listener: OnDateSetListener) {
//        this.onDateSetListener = listener
//    }


//    fun showBottomSheet() {
//        val datePickerBottomSheet = DateWheelPickerFragment()
//        datePickerBottomSheet.show(requireActivity().supportFragmentManager, datePickerBottomSheet.tag)
//    }
}