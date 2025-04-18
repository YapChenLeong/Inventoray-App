package com.example.inventory.repository

import com.example.inventory.data.*
import kotlinx.coroutines.flow.Flow
import java.util.*

class InventoryRepository(private val itemDao: ItemDao) {

    suspend fun insertItem(item: Item){
        itemDao.insert(item)
    }

    suspend fun insertTransactionData(item: TransactionListData){
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
    fun getAllTransactionListData() : Flow<List<TransactionListData>>{
        return itemDao.getAllTransactionListInfo();
    }
    fun getTransactionDataFromMonth(month: String, year: String, type: String) : Flow<List<TransactionListData>>{
        return itemDao.getTransactionDataFromMonth(month, year, type);
    }
    fun getSingleItem(id: Int): Flow<Item> {
        return itemDao.getItem(id)
    }

    fun getGroupCountryItem(): Flow<List<StatisticHeader>>{
        return itemDao.getGroupedItems()
    }
//
//    suspend fun insertDefaultItems(item: ExpenseData){
//        itemDao.insert(item)
//    }

    /** Setting Data Control*/
    suspend fun insertSettingData(item: SettingData){
        itemDao.insertSettingData(item)
    }

    fun getSettingData(): Flow<List<SettingData>> {
        return itemDao.getSettingData()
    }

    /** Default Push Expense Data */
    suspend fun autoInsertExpenseData(item: List<ExpenseData>){
        itemDao.autoInsertExpenseData(item)
    }

    suspend fun autoInsertIncomeData(item: List<IncomeData>){
        itemDao.autoInsertIncomeData(item)
    }

    fun getAllExpenseData(): Flow<List<ExpenseData>> {
        return itemDao.getAllExpenseData()
    }
    fun getExpenseData(): Flow<List<ExpenseDataWithSubExpense?>> {
        return itemDao.getExpenseData()
    }
    fun getIncomeData(): Flow<List<IncomeDataWithSubIncome?>> {
        return itemDao.getIncomeData()
    }

    suspend fun insertNewCategory(item: ExpenseData){
        itemDao.insertNewCategory(item)
    }

    fun getAllSubCategory(): Flow<List<SubCategoryData>> {
        return itemDao.getAllSubCategory()
    }

    fun getSubCategoryData(expenseId: String): Flow<List<SubCategoryData?>> {
        return itemDao.getSubCategoryData(expenseId)
    }
    suspend fun insertNewSubCategory(item: SubExpenseData) {
        itemDao.insertNewSubCategory(item)
    }

    suspend fun deleteSubCategoryData(susExpendId: String) {
        return itemDao.deleteSubCategoryData(susExpendId)
//        itemDao.deleteSubCategoryData(susExpendId)
    }

    suspend fun deleteExpenseAndSubItems(expenseId: String) {
        return itemDao.deleteExpenseAndSubItems(expenseId)
    }

    suspend fun updateExpenseItem(expenseId: String, dtUpdate: Date, position: Int) {
        return itemDao.updateExpenseItem(expenseId, dtUpdate, position)
    }
    suspend fun updateExpenseListIndex(items: List<ExpenseData>) {
        return itemDao.updateExpenseListIndex(items)
    }





    companion object {
        private const val PREF_KEY_DEFAULT_ITEMS_INSERTED = "default_items_inserted"
    }

}