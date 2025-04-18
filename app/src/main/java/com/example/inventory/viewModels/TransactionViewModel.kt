package com.example.inventory.viewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import com.example.inventory.data.TransactionListData
import com.example.inventory.repository.InventoryRepository
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.atomic.AtomicBoolean

class TransactionViewModel (private val itemDao: ItemDao): ViewModel(){
    private lateinit var repository: InventoryRepository

    init {
        viewModelScope.launch {
            repository = InventoryRepository(itemDao)
        }
    }

    var getAllTransactionData: LiveData<List<TransactionListData>> = repository.getAllTransactionListData().asLiveData()


    fun getTransactionDataFromMonth(month: String, year: String, type: String): LiveData<List<TransactionListData>> {
        return repository.getTransactionDataFromMonth(month, year, type).asLiveData()
    }
    /**#################################### INSERT DATA  *####################################*/
    /**
     * Inserts the new Item into database.
     */
    fun addNewTransaction(imgResourceID: Int, type: String, category: String, detail: String, amountValue: String, itemDate: Date) {
        val newItem = getNewTransactionEntry(imgResourceID, type, category, detail, amountValue, itemDate)
        insertTransaction(newItem)
    }

    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewTransactionEntry(imgResourceID: Int, type: String, category: String, detail: String, amountValue: String, itemDate: Date): TransactionListData {
        return TransactionListData(
            imgResourceID = imgResourceID,
            type = type,
            itemCategory = category,
            description01 = detail,
            description02 = null,
            description03 = null,
            dtCreate = itemDate,
            dtUpdate = itemDate,
            amountValue = amountValue

        )
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertTransaction(item: TransactionListData) {
        viewModelScope.launch {
            repository.insertTransactionData(item)
//            itemDao.insert(item) //without repo use this code
        }
    }
    /**
     * Notice that you did not use viewModelScope.launch for addNewItem(), but it is needed above in insertItem() when you call a DAO method.
     * The reason is that the suspend functions are only allowed to be called from a coroutine or another suspend function.
     * The function itemDao.insert(item)is a suspend function.
     * */

    /**#################################### END INSERT DATA  *####################################*/

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(type: String, itemDescription: String, itemPrice: String): Boolean {
        if (type.isBlank() || itemDescription.isBlank() || itemPrice.isBlank()) {
            return false
        }
        return true
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 * Pass in the same constructor parameter as the InventoryViewModel that is the ItemDao instance
 */
class TransactionViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        /**
         * Check if the modelClass is the same as the InventoryViewModel class and return an instance of it.
         * Otherwise, throw an exception.
         *
         * Tips: The creation of the ViewModel factory is mostly boilerplate code, so you can reuse this code for future ViewModel factories.
         * */
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TransactionSingleEventLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: androidx.lifecycle.Observer<in T>) {
        super.observe(owner) { value ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }
}