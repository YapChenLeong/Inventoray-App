package com.example.inventory.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CategoryLists(
    @StringRes val stringResourceId: Int,
    @DrawableRes val iconResourceId: Int,
    val iconName: String,
    val category: String

)
