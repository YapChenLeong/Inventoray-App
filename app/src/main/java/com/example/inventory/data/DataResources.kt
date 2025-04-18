package com.example.inventory.data

import com.example.inventory.R

class DataResources(){

    fun loadExpensesData(): List<ExpenseItems> {
        val subFoods = arrayListOf(
            SubCategoryLists("A100","B100", "Bread"),
            SubCategoryLists("A100","B100", "Burger"),
            SubCategoryLists("A100","B100", "Sushi"),
            SubCategoryLists("A100","B100", "Subway"))

        val subTransport = arrayListOf(
            SubCategoryLists("A101","B101", "Train"),
            SubCategoryLists("A101","B101", "Car"),
            SubCategoryLists("A101","B101", "Bus"),
            SubCategoryLists("A101","B101", "Motor"))

        val beautyNutritionItems = arrayListOf(
            SubCategoryLists("A102","B102", "Cosmetic"),
            SubCategoryLists("A102","B102", "Makeup"),
            SubCategoryLists("A102","B102", "Skin care"),
            SubCategoryLists("A102","B102", "Skin mask"),
            SubCategoryLists("A102","B102", "Toothpaste"))



        return listOf<ExpenseItems>(
            ExpenseItems(R.string.item1, R.drawable.food,"Expense",subFoods),
            ExpenseItems(R.string.item6, R.drawable.transport, "Expense", subTransport),
            ExpenseItems(R.string.item3, R.drawable.medical, "Expense",emptyList()),
            ExpenseItems(R.string.item4, R.drawable.sport, "Expense",emptyList()),
            ExpenseItems(R.string.item5, R.drawable.beauty, "Expense",beautyNutritionItems),
            ExpenseItems(R.string.item6, R.drawable.transport, "Expense",emptyList()),
            ExpenseItems(R.string.item7, R.drawable.travel, "Expense",emptyList()),
            ExpenseItems(R.string.item8, R.drawable.nutrition, "Expense",emptyList()),
            ExpenseItems(R.string.item9, R.drawable.skincare, "Expense",emptyList()),
            ExpenseItems(R.string.item10, R.drawable.pet,"Expense",emptyList()),
            ExpenseItems(R.string.item11, R.drawable.donation,"Expense",emptyList()),
            ExpenseItems(R.string.item12, R.drawable.utilities,"Expense",emptyList()),
            ExpenseItems(R.string.item2, R.drawable.movie,"Expense",emptyList()),
            ExpenseItems(R.string.item3, R.drawable.medical,"Expense",emptyList()),
            ExpenseItems(R.string.item4, R.drawable.sport,"Expense",emptyList()),
            ExpenseItems(R.string.item5, R.drawable.beauty,"Expense",emptyList()),
            ExpenseItems(R.string.item6, R.drawable.transport,"Expense",emptyList()),
            ExpenseItems(R.string.item7, R.drawable.travel,"Expense",emptyList()),
            ExpenseItems(R.string.item8, R.drawable.nutrition,"Expense",emptyList()),
            ExpenseItems(R.string.item9, R.drawable.skincare,"Expense",emptyList()),
            ExpenseItems(R.string.item10, R.drawable.pet,"Expense",emptyList()),
            ExpenseItems(R.string.item12, R.drawable.utilities,"Expense",emptyList()),
            ExpenseItems(R.string.item12, R.drawable.utilities,"Expense",emptyList()),
            ExpenseItems(R.string.item2, R.drawable.medical,"Expense",emptyList()),
            ExpenseItems(R.string.item6, R.drawable.travel,"Expense",emptyList()),
            ExpenseItems(R.string.addCategory, R.drawable.ic_add_circle_outline_black,"AddExItem",emptyList()),
            ExpenseItems(R.string.adjustCategory, R.drawable.ic_setting_black,"AdjustExItem",emptyList()))
    }
}
