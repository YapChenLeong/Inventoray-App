package com.example.inventory.data

data class ExpenseDataWithSubExpense (
    val expenseId: String,
    val expenseName: String,
    val imageResourceId: Int,
    val type: String,
    val subExpenseId: String?,
    val subExpenseName: String?,
    val subExpenseCount: Int
)

