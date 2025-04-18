package com.example.inventory.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import java.time.LocalDate

class SharedStatisticViewModel : ViewModel() {
    private val _selectedMonthYear = MutableLiveData<LocalDate?>()

    val selectedDate: LiveData<LocalDate?> = _selectedMonthYear


    fun updateDate(date: LocalDate?) {
        _selectedMonthYear.value = date
    }
}