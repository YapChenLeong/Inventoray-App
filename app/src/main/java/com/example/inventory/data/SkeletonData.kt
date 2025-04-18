package com.example.inventory.data

import com.example.inventory.R
import java.util.*

class SkeletonData(){

    fun insertExpensesData(): List<ExpenseData> {

        var inputDate : Date = Calendar.getInstance().time

        return listOf<ExpenseData>(
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.food, "Expense", "Food",null,inputDate,inputDate,0),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.utilities, "Expense", "Electric",null,inputDate,inputDate,1),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.grab_car, "Expense", "Car",null,inputDate,inputDate,2),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.petrol, "Expense", "Petrol",null,inputDate,inputDate,3),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.parking, "Expense", "Parking",null,inputDate,inputDate,4),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.medical, "Expense", "Medical",null,inputDate,inputDate,5),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.bread, "Expense", "Bakery",null,inputDate,inputDate,6),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.toothpaste, "Expense", "Daily product",null,inputDate,inputDate,7),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.skincare, "Expense", "Skin care",null,inputDate,inputDate,8),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.beauty, "Expense", "Beauty",null,inputDate,inputDate,9),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.transport, "Expense", "Train",null,inputDate,inputDate,10),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.travel, "Expense", "Travel",null,inputDate,inputDate,11),
            ExpenseData(ExpenseData.generateSysGuid(), R.drawable.movie, "Expense", "Movie",null,inputDate,inputDate,12),
        )
    }

    fun insertIncomeData(): List<IncomeData> {
        var inputDate : Date = Calendar.getInstance().time

        return listOf<IncomeData>(
            IncomeData(IncomeData.generateSysGuid(), R.drawable.money_box, "Income", "Salary",null,inputDate,inputDate,0),
            IncomeData(IncomeData.generateSysGuid(), R.drawable.money_box, "Income", "Allowance",null,inputDate,inputDate,1),
            IncomeData(IncomeData.generateSysGuid(), R.drawable.money_box, "Income", "Bonus",null,inputDate,inputDate,2),
            )
    }


}
