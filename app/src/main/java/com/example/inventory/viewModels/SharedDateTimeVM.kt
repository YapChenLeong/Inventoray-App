package com.example.inventory.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedDateTimeVM: ViewModel() {
    private val _selectedDate = MutableLiveData<Triple<Int, Int, Int>>() // Day, Month, Year
    private val _selectedTime = MutableLiveData<Triple<Int, Int, Int>>() // Hour, Minute, AMPM
    private val _selectedMonth = MutableLiveData<String>() // Month


    var selectedDate: LiveData<Triple<Int, Int, Int>> = _selectedDate
    var selectedTime: LiveData<Triple<Int, Int, Int>> = _selectedTime
    var selectedMonth: LiveData<String> = _selectedMonth

    fun setSelectedDate(day: Int, month: Int, year: Int) {
        _selectedDate.value = Triple(day, month, year)
    }
    fun getSelectedDate(): Triple<Int,Int,Int>? {
        return _selectedDate.value  // Returns the Triple<Int, Int, Int> directly
    }


    fun setSelectedTime(hour: Int, minute: Int, ampm: Int) {
        _selectedTime.value = Triple(hour, minute, ampm)
    }
    fun getSelectedTime(): Triple<Int,Int,Int>? {
        return _selectedTime.value  // Returns the Triple<Int, Int, Int> directly
    }

    fun setSelectedMonth(month: String) {
        _selectedMonth.value = month
    }

}