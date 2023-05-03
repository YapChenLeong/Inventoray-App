package com.example.inventory.repository

import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.flow.Flow

class InventoryRepository(private val itemDao: ItemDao) {

    suspend fun insertItem(item: Item){
        itemDao.insert(item)
    }

    suspend fun updateItem(item: Item){
        itemDao.update(item)
    }

    suspend fun deleteItem(item: Item){
        itemDao.delete(item)
    }

    fun getAllItems(): Flow<List<Item>> {
        return itemDao.getItems()
    }
    fun getSingleItem(id: Int): Flow<Item> {
        return itemDao.getItem(id)
    }
}