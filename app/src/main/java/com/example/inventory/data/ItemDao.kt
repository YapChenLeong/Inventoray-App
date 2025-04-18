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
package com.example.inventory.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Database access object to access the Inventory database
 */
@Dao
interface ItemDao {

    @Query("SELECT * from Item ORDER BY date DESC")
    fun getItems(): Flow<List<Item>>

    @Query("SELECT * from T_TRANSACTION_DATA ORDER BY DT_CREATE DESC")
    fun getAllTransactionListInfo(): Flow<List<TransactionListData>>

    @Query("""
    SELECT * FROM T_TRANSACTION_DATA 
    WHERE strftime('%m', DT_CREATE / 1000, 'unixepoch') = :month 
    AND strftime('%Y', DT_CREATE / 1000, 'unixepoch') = :year
    AND TYPE = :transactionType
""")
    fun getTransactionDataFromMonth(month: String, year: String, transactionType: String): Flow<List<TransactionListData>>

    @Query("SELECT * from item WHERE id = :id")
    fun getItem(id: Int): Flow<Item>

//    @Query("SELECT country, SUM(price) AS total_price, 100.0 * COUNT(*) / (SELECT COUNT(*) FROM items) AS percentage FROM items GROUP BY country")
    @Query("SELECT country, SUM(price) AS totalPrice, (SUM(price) * 100.0) / (SELECT SUM(price) FROM item) AS percentage FROM item GROUP BY country")
//    @Query("SELECT country, COUNT(*) AS itemCount FROM item GROUP BY country")
    fun getGroupedItems(): Flow<List<StatisticHeader>>

    @Query("SELECT * from T_Settings")
    fun getSettingData(): Flow<List<SettingData>>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Item into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: TransactionListData)

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(item: ExpenseData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSettingData(settingData: SettingData)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun autoInsertExpenseData(item: List<ExpenseData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun autoInsertIncomeData(item: List<IncomeData>)

    @Query("SELECT * FROM T_Expense ORDER BY ORDER_INDEX ASC")
    fun getAllExpenseData(): Flow<List<ExpenseData>>

    @Query("select e.ID as expenseId, e.DSCP_01 as expenseName, e.IMG_RESOURCE_ID as imageResourceId, e.TYPE as type, se.ID as subExpenseId, se.DSCP_01 as subExpenseName, (SELECT COUNT(*) FROM T_SubExpense WHERE EXPENSE_ID = e.ID) AS subExpenseCount \n" +
            "from T_Expense e \n" +
            "left join T_SubExpense se ON e.ID = se.EXPENSE_ID ORDER BY e.ORDER_INDEX ASC")
    fun getExpenseData(): Flow<List<ExpenseDataWithSubExpense?>>

    @Query("select i.ID as incomeId, i.DSCP_01 as incomeName, i.IMG_RESOURCE_ID as imageResourceId, i.TYPE as type, si.ID as subExpenseId, si.DSCP_01 as subIncomeName, (SELECT COUNT(*) FROM T_SubIncome WHERE INCOME_ID = i.ID) AS subIncomeCount \n" +
            "from T_Income i \n" +
            "left join T_SubIncome si ON i.ID = si.INCOME_ID ORDER BY i.ORDER_INDEX ASC")
    fun getIncomeData(): Flow<List<IncomeDataWithSubIncome?>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewCategory(item: ExpenseData)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewSubCategory(item: SubExpenseData)

    @Query("SELECT se.ID as id,\n" +
            "    se.EXPENSE_ID as expenseId,\n" +
            "    se.DSCP_01 as description01,\n" +
            "    se.DSCP_02 as description02,\n" +
            "    se.DT_CREATE as dtCreate,\n" +
            "    se.DT_UPDATE as dtUpdate, e.IMG_RESOURCE_ID as imageResourceId from T_SubExpense se left join T_Expense e on se.EXPENSE_ID = e.ID ORDER BY se.DT_UPDATE ASC")
    fun getAllSubCategory(): Flow<List<SubCategoryData>>

    @Query("SELECT se.ID as id,\n" +
            "    se.EXPENSE_ID as expenseId,\n" +
            "    se.DSCP_01 as description01,\n" +
            "    se.DSCP_02 as description02,\n" +
            "    se.DT_CREATE as dtCreate,\n" +
            "    se.DT_UPDATE as dtUpdate, e.ID as ExpenseId, e.IMG_RESOURCE_ID as imageResourceId from T_SubExpense se left join T_Expense e on se.EXPENSE_ID = e.ID  WHERE e.ID = :expId ORDER BY se.DT_UPDATE ASC")
//    @Query("select * from T_SubExpense where EXPENSE_ID = :expId")
    fun getSubCategoryData(expId: String) : Flow<List<SubCategoryData>>

    @Query("DELETE FROM T_SubExpense WHERE ID = :susExpendId")
    suspend fun deleteSubCategoryData(susExpendId: String)

    suspend fun deleteExpenseAndSubItems(expenseId: String){
        deleteSubExpenseItems(expenseId)
        deleteExpenseItem(expenseId)
    }
    @Query("DELETE FROM T_SubExpense WHERE EXPENSE_ID = :expenseId")
    suspend fun deleteSubExpenseItems(expenseId: String)

    @Query("DELETE FROM T_Expense WHERE ID = :expenseId")
    suspend fun deleteExpenseItem(expenseId: String)

    @Query("UPDATE T_Expense SET DT_UPDATE = :dt_update, ORDER_INDEX = :position WHERE ID = :expenseId")
    suspend fun updateExpenseItem(expenseId: String, dt_update: Date, position: Int)

    suspend fun updateExpenseListIndex(items: List<ExpenseData>){
        for ((index, item) in items.withIndex()){
            updateExpenseListIndex(index, item.id)
        }
    }
    @Query("UPDATE T_Expense SET ORDER_INDEX = :index WHERE ID = :expenseId")
    suspend fun updateExpenseListIndex(index: Int, expenseId: String)


}
