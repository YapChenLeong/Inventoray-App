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

package com.example.inventory.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.inventory.data.*
import com.example.inventory.repository.InventoryRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import java.util.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * View Model to keep a reference to the Inventory repository and an up-to-date list of all items.
 * ViewModel will interact with the database via the DAO, and provide data to the UI.
 * All database operations will have to be run away from the main UI thread, you'll do that using coroutines and viewModelScope.
 */
class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    private lateinit var repository: InventoryRepository

    init {
        viewModelScope.launch {
            repository = InventoryRepository(itemDao)
        }
    }

    /**
     * Retrieve all items from the repository.
     * Cache all items form the database using LiveData.
     * The getItems() function returns a Flow.
     * To consume the data as a LiveData value, use the asLiveData() function.
     */
    var allItems: LiveData<List <Item>> = repository.getAllItems().asLiveData()// collect each item from the Flow, then convert any Flow to LiveData
//    val allItems: LiveData<List<Item>> = itemDao.getItems().asLiveData() // without repo use this code
//    fun allItems(): LiveData<List <Item>>{
//        return repository.getAllItems().asLiveData()
//    }
    /**
     * Retrieve single item from the repository.
     */
    fun retrieveItem(id: Int): LiveData<Item> {
        return repository.getSingleItem(id).asLiveData()
//        return itemDao.getItem(id).asLiveData() //without repo use this code
    }

    var groupItem: LiveData<List<StatisticHeader>> = repository.getGroupCountryItem().asLiveData()


    /**#################################### UPDATE DATA  *####################################*/
    /**
     * Updates an existing Item in the database.
     */
    fun updateItem(
        itemId: Int,
        itemCountry: String,
        itemName: String,
        itemPrice: String,
        itemCount: String,
        itemDate: Date
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemCountry, itemName, itemPrice, itemCount, itemDate)
        updateItem(updatedItem)
    }

    /**
     * Called to update an existing entry in the Inventory database.
     * Returns an instance of the [Item] entity class with the item info updated by the user.
     */
    private fun getUpdatedItemEntry(
        itemId: Int,
        itemCountry: String,
        itemName: String,
        itemPrice: String,
        itemCount: String,
        itemDate: Date
    ): Item {
        return Item(
            id = itemId,
            country = itemCountry,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            date = itemDate
        )
    }

    /**
     * Launching a new coroutine to update an item in a non-blocking way
     */
    private fun updateItem(item: Item) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }
    /**#################################### END UPDATE DATA  *####################################*/


    /**#################################### INSERT DATA  *####################################*/
    /**
     * Inserts the new Item into database.
     */
    fun addNewItem(itemCountry: String, itemName: String, itemPrice: String, itemCount: String, itemDate: Date) {
        val newItem = getNewItemEntry(itemCountry, itemName, itemPrice, itemCount, itemDate)
        insertItem(newItem)
    }

    /**
     * Returns an instance of the [Item] entity class with the item info entered by the user.
     * This will be used to add a new entry to the Inventory database.
     */
    private fun getNewItemEntry(itemCountry: String, itemName: String, itemPrice: String, itemCount: String, itemDate: Date): Item {
        return Item(
            country = itemCountry,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt(),
            date = itemDate
        )
    }

    /**
     * Launching a new coroutine to insert an item in a non-blocking way
     */
    private fun insertItem(item: Item) {
        viewModelScope.launch {
            repository.insertItem(item)
//            itemDao.insert(item) //without repo use this code
        }
    }
    /**
     * Notice that you did not use viewModelScope.launch for addNewItem(), but it is needed above in insertItem() when you call a DAO method.
     * The reason is that the suspend functions are only allowed to be called from a coroutine or another suspend function.
     * The function itemDao.insert(item)is a suspend function.
     * */
    /**#################################### END INSERT DATA  *####################################*/


    /**#################################### DELETE DATA  *####################################*/
    /**
     * Launching a new coroutine to delete an item in a non-blocking way
     */
    fun deleteItem(item: Item) {
        viewModelScope.launch {
            repository.deleteItem(item)
//            itemDao.delete(item) //without repo use this code
        }
    }
    /**#################################### END DELETE DATA  *####################################*/


    /**
     * Returns true if stock is available to sell, false otherwise.
     */
    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    /**
     * Decreases the stock by one unit and updates the database.
     */
    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    /**
     * Returns true if the EditTexts are not empty
     */
    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    var getSettingData: LiveData<List<SettingData>> = repository.getSettingData().asLiveData()// collect each item from the Flow, then convert any Flow to LiveData


//    fun getSettingData(){
//        viewModelScope.launch {
//            repository.getSettingData()
//        }
//    }
    fun insertSettingData(item : SettingData){
        viewModelScope.launch {
            repository.insertSettingData(item)
        }
    }

    private val _insertionStatus = MutableLiveData<Boolean>()
    val insertionStatus: LiveData<Boolean>
        get() = _insertionStatus
    fun autoInsertExpenseData(item : List<ExpenseData>){
        viewModelScope.launch {
            try {
                repository.autoInsertExpenseData(item)
                _insertionStatus.value = true
            } catch (e:Exception){
                // Handle any exceptions, such as database errors
                _insertionStatus.value = false
            }

        }
    }

    private val _insertionIncomeStatus = MutableLiveData<Boolean>()
    val insertionIncomeStatus: LiveData<Boolean>
        get() = _insertionIncomeStatus
    fun autoInsertIncomeData(item : List<IncomeData>){
        viewModelScope.launch {
            try {
                repository.autoInsertIncomeData(item)
                _insertionIncomeStatus.value = true
            } catch (e:Exception){
                // Handle any exceptions, such as database errors
                _insertionIncomeStatus.value = false
            }

        }
    }

    /**
     * Category Module
     * */
    var getExpenseData: LiveData<List<ExpenseDataWithSubExpense?>> = repository.getExpenseData().asLiveData()// collect each item from the Flow, then convert any Flow to LiveData
    var getIncomeData: LiveData<List<IncomeDataWithSubIncome?>> = repository.getIncomeData().asLiveData()// collect each item from the Flow, then convert any Flow to LiveData

    private val _getAllExpenseData = SingleEventLiveData<Boolean>()
    val getAllExpenseData: LiveData<Boolean> get() = _getAllExpenseData
    fun getAllExpenseData(): LiveData<List<ExpenseData>> {
            return repository.getAllExpenseData().asLiveData()
            _getAllExpenseData.value = true
    }
//    var getAllExpenseData: LiveData<List<ExpenseData>> = repository.getAllExpenseData().asLiveData()

    fun isEntryValid2(itemName: String): Boolean {
        if (itemName.isBlank()) {
            return false
        }
        return true
    }

    fun addNewCategory(image: Int, type: String, itemName: String, dtCreate:Date, dtUpdate:Date, orderIndex: Int) {
        val newItem = getNewItemEntry(ExpenseData.generateSysGuid(), image, type, itemName,"",dtCreate,dtUpdate,orderIndex)
        insertCategory(newItem)
    }
    private fun getNewItemEntry(itemId: String, image: Int, type: String, desc1: String, desc2: String, dtCreate: Date, dtUpdate: Date, orderIndex: Int): ExpenseData {
        return ExpenseData(
            id = itemId,
            imgResourceID = image,
            type = type,
            description01 = desc1,
            description02 = desc2,
            dtCreate = dtCreate,
            dtUpdate = dtUpdate,
            orderIndex = orderIndex
        )
    }
    private fun insertCategory(item: ExpenseData) {
        viewModelScope.launch {
            repository.insertNewCategory(item)
//            itemDao.insert(item) //without repo use this code
        }
    }

    fun addNewSubCategory(expenseId: String, subItemName: String, dtCreate:Date, dtUpdate:Date) {
        val newItem = getNewSubItemEntry(SubExpenseData.generateSysGuid(), expenseId, subItemName, "",dtCreate,dtUpdate)
        insertSubCategory(newItem)
    }
    private fun getNewSubItemEntry(subId: String, expenseId: String, desc1: String, desc2: String, dtCreate: Date, dtUpdate: Date): SubExpenseData {
        return SubExpenseData(
            id = subId,
            expenseId = expenseId,
            description01 = desc1,
            description02 = desc2,
            dtCreate = dtCreate,
            dtUpdate = dtUpdate
        )
    }

    private fun insertSubCategory(item: SubExpenseData){
        viewModelScope.launch {
            repository.insertNewSubCategory(item)
        }
    }

    fun updateExpenseItem(expenseId: String, dtUpdate: Date, position: Int) {
        viewModelScope.launch {
            repository.updateExpenseItem(expenseId, dtUpdate, position)
        }
    }

    private val _updateExpenseListIndex = SingleEventLiveData<Boolean>()
    val updateExpenseListIndexBoolean: LiveData<Boolean> get() = _updateExpenseListIndex
    fun updateExpenseListIndex(items: List<ExpenseData>) {
        viewModelScope.launch {
            try {
                repository.updateExpenseListIndex(items)
                _updateExpenseListIndex.value = true
            }catch (e:Exception){
                _updateExpenseListIndex.value = false
            }
        }
    }

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean>
        get() = _deleteSuccess

    fun deleteSubCategoryData(susExpendId: String) {
        viewModelScope.launch {
            try {
                repository.deleteSubCategoryData(susExpendId)
                _deleteSuccess.value = true
            } catch (e:Exception){
                _deleteSuccess.value = false
            }
        }
    }

    var allSubCategoryData: LiveData<List<SubCategoryData>> = repository.getAllSubCategory().asLiveData()// collect each item from the Flow, then convert any Flow to LiveData

    fun getSubCategoryData(expenseId: String): LiveData<List<SubCategoryData?>> {
        return repository.getSubCategoryData(expenseId).asLiveData()
//        return itemDao.getItem(id).asLiveData() //without repo use this code
    }

    private val _deleteExpenseAndSubItems = SingleEventLiveData<Boolean>()
    val deleteExpenseAndSubItems: LiveData<Boolean> get() = _deleteExpenseAndSubItems
    fun deleteExpenseAndSubItems(expenseId: String){
        viewModelScope.launch {
            try {
                repository.deleteExpenseAndSubItems(expenseId)
                _deleteExpenseAndSubItems.value = true
            }catch (e: Exception){
                val error = e
                Log.i("error delete", e.toString())
                _deleteExpenseAndSubItems.value = false
            }
        }
    }
}

/**
 * Factory class to instantiate the [ViewModel] instance.
 * Pass in the same constructor parameter as the InventoryViewModel that is the ItemDao instance
 */
class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        /**
         * Check if the modelClass is the same as the InventoryViewModel class and return an instance of it.
         * Otherwise, throw an exception.
         *
         * Tips: The creation of the ViewModel factory is mostly boilerplate code, so you can reuse this code for future ViewModel factories.
         * */
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class SingleEventLiveData<T> : MutableLiveData<T>() {
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

