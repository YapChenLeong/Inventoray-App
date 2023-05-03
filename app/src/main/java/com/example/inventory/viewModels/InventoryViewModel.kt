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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import com.example.inventory.repository.InventoryRepository
import kotlinx.coroutines.launch
import java.util.*

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

