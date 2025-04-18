package com.example.inventory.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.activityViewModels
import com.example.inventory.R
import com.example.inventory.databinding.FragmentDateWheelPickerBinding
import com.example.inventory.databinding.FragmentTimeWheelPickerBinding
import com.example.inventory.viewModels.SharedDateTimeVM
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class TimeWheelPickerFragment : Fragment() {

    private var _binding: FragmentTimeWheelPickerBinding? = null
    private val binding get() = _binding!!
    private val sharedDateTimeVM: SharedDateTimeVM by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTimeWheelPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Configure the hour picker
        binding.hourPicker.minValue = 1
        binding.hourPicker.maxValue = 12

        //Configure the minute picker
        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59

        //Configure the AM/PM picker
        binding.ampmPicker.minValue = 0
        binding.ampmPicker.maxValue = 1

        val calendar = Calendar.getInstance()
        val currentHour24 = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentSecond = calendar.get(Calendar.SECOND)

        // Convert 24-hour format to 12-hour format
        val currentHour12 = if (currentHour24 > 12) (currentHour24 - 12) else currentHour24

        val displayedValues = Array( binding.hourPicker.maxValue -  binding.hourPicker.minValue + 1) { i ->
            String.format("%02d", i +  binding.hourPicker.minValue)
        }

        //Get Time from live data
        val liveSelectedTime = sharedDateTimeVM.getSelectedTime()

        binding.hourPicker.value = if(liveSelectedTime != null) liveSelectedTime.first else currentHour12

        // Create an array of strings with two-digit numbers
        val values = Array(60) { index -> String.format("%02d", index) }

        binding.minutePicker.displayedValues = values
        binding.minutePicker.value = if(liveSelectedTime != null) liveSelectedTime.second else currentMinute

        // Set AM/PM value based on current hour
        binding.ampmPicker.displayedValues = arrayOf("AM", "PM")
        binding.ampmPicker.value = if(liveSelectedTime != null) liveSelectedTime.third else if (currentHour24 <= 12) 0 else 1

        binding.hourPicker.setOnValueChangedListener { _, _, newHour ->
            sharedDateTimeVM.setSelectedTime(newHour, binding.minutePicker.value, binding.ampmPicker.value)
        }
        binding.minutePicker.setOnValueChangedListener { _, _, newMinute ->
            sharedDateTimeVM.setSelectedTime(binding.hourPicker.value, newMinute, binding.ampmPicker.value)
        }
        binding.ampmPicker.setOnValueChangedListener { _, _, newAmPM ->
            sharedDateTimeVM.setSelectedTime(binding.hourPicker.value, binding.minutePicker.value, newAmPM)
        }
    }


}