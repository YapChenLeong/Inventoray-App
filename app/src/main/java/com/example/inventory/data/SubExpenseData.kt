package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "T_SubExpense")
data class SubExpenseData(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String,
    @ColumnInfo(name = "EXPENSE_ID")
    val expenseId: String,
    @ColumnInfo(name = "DSCP_01")
    val description01: String,
    @ColumnInfo(name = "DSCP_02")
    val description02: String?, // make it nullable
    @ColumnInfo(name = "DT_CREATE")
    val dtCreate: Date?,
    @ColumnInfo(name = "DT_UPDATE")
    val dtUpdate: Date?
){
    companion object {
        fun generateSysGuid(): String {
            // Implement logic to generate SYS_GUID()
            // For example, using java.util.UUID.randomUUID().toString() or any other method
            return java.util.UUID.randomUUID().toString()
        }
    }
}
