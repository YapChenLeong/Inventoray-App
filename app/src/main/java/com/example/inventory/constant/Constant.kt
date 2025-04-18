package com.example.inventory.constant


class Constant() {

    //Configure Date/Time
    val minDay = 1
    val maxDay = 31
    val minMonth = 0//zero = Jan
    val maxMonth = 11 //December
    val minYear = 1900
    fun getMonthsList(): List<String>{
        return listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
    }
}