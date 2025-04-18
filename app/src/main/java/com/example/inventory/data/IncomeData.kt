package com.example.inventory.data

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "T_Income")
data class IncomeData(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String,
    @ColumnInfo(name = "IMG_RESOURCE_ID")
    @DrawableRes val imgResourceID: Int,
    @ColumnInfo(name = "TYPE")
    val type: String,
    @ColumnInfo(name = "DSCP_01")
    val description01: String,
    @ColumnInfo(name = "DSCP_02")
    val description02: String?, // make it nullable
    @ColumnInfo(name = "DT_CREATE")
    val dtCreate: Date?,
    @ColumnInfo(name = "DT_UPDATE")
    val dtUpdate: Date?,
    @ColumnInfo(name = "ORDER_INDEX")
    val orderIndex: Int
) {
    companion object {
        fun generateSysGuid(): String {
            // Implement logic to generate SYS_GUID()
            // For example, using java.util.UUID.randomUUID().toString() or any other method
            return java.util.UUID.randomUUID().toString()
        }
    }
}
