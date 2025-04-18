package com.example.inventory.data

import androidx.room.*
import java.text.NumberFormat
import java.util.*


data class StatisticHeader (
    val country: String,
    val totalPrice: Double,
    val percentage: Double
    )

