package com.example.inventory.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ExpenseItems(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int,
    val type: String,
    val mList: List<SubCategoryLists>
)