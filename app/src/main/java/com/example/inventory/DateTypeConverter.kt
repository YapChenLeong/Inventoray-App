package com.example.inventory

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {
    /**
     * Suppose you need to persist instances of Date in your Room database.
     * Room doesn't know how to persist Date objects, so you need to define type converters:
     *
     * This example defines two type converter methods:
     * one that converts a Date object to a Long object,
     * one that performs the inverse conversion from Long to Date
     * */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
//    @TypeConverter
//    fun fromTimestamp(timeStamp: Long?): String? {
//        return timeStamp?.let { FORMATTER.format(timeStamp) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(timeStamp: String?): Long? {
//        return timeStamp?.let { FORMATTER.parse(it)?.time }
//    }
//
//    companion object{
//        val FORMATTER = SimpleDateFormat("dd-MM-yyyy")
//    }
}