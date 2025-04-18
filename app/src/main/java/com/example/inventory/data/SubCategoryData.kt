package com.example.inventory.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.*

data class SubCategoryData(
    val id: String,
    val expenseId: String,
    val description01: String,
    val description02: String?, // make it nullable
    val dtCreate: Date?,
    val dtUpdate: Date?,
    val imageResourceId: Int
)
