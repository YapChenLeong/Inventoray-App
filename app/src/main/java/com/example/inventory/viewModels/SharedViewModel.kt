package com.example.inventory.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _itemImageResource = MutableLiveData<Int>()
    val itemImageResource: LiveData<Int> get() = _itemImageResource

    fun setItemImageResource(resourceId: Int) {
        _itemImageResource.value = resourceId
    }
}