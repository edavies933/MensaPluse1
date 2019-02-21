package com.example.emmanueldavies.mensapluse1.rooom

import android.annotation.TargetApi
import android.arch.persistence.room.TypeConverter
import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class DateTypeConverter {

    @TargetApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String): LocalDate? {
        val string = "January 2, 2010"
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(string, formatter)
        return  localDate
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): String? {
        return date.toString()
    }
}