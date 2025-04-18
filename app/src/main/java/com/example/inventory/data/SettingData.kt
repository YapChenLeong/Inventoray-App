package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "T_Settings")
data class SettingData(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: String = generateSysGuid(),
    @ColumnInfo(name = "DEFAULT_ITEMS")
    val defaultValue: String,
    @ColumnInfo(name = "DT_CREATE")
    val dt_create : Date,
    @ColumnInfo(name = "DT_UPDATE")
    val dt_update : Date

){
    companion object {
        fun generateSysGuid(): String {
            // Implement logic to generate SYS_GUID()
            // For example, using java.util.UUID.randomUUID().toString() or any other method
            return java.util.UUID.randomUUID().toString()
        }
    }
}

