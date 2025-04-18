package com.example.inventory.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class HeaderCategory(
    val headId: String,
    val imageResourceId: Int,
    val description: String,
    val type: String,
    val subSize: Int
)