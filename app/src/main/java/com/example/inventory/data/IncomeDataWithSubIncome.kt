package com.example.inventory.data

data class IncomeDataWithSubIncome (
    val incomeId: String,
    val incomeName: String,
    val imageResourceId: Int,
    val type: String,
    val subIncomeId: String?,
    val subIncomeName: String?,
    val subIncomeCount: Int
)