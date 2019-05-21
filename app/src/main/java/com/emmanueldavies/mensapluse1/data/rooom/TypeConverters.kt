package com.emmanueldavies.mensapluse1.data.rooom

import android.arch.persistence.room.TypeConverter
import com.emmanueldavies.mensapluse1.domain.model.Prices

class DateTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun notesToStringConverter(notes: List<String>): String {
            var result = ""
            for ((index, note) in notes.withIndex()) {

                result = if (index == 0) {
                    "$note"
                } else {
                    "$result, $note"

                }

            }
            return result
        }

        @TypeConverter
        @JvmStatic
        fun stringToNoteConverter(string: String): List<String> {
            var noteList = string.replace(" ", "").split(",")
            return noteList
        }

        @TypeConverter
        @JvmStatic
        fun stringToPricesConvert(string: String): Prices {

            var valueList = string.replace("Student:", "").replace("Employee:", "")
                .replace("Others:", "")

            valueList = valueList.replace(" ", "")

            var splitedList = valueList.split("€,")

            return Prices(
                splitedList[0],
                splitedList[1],
                others = splitedList[2].replace("€", "")
            )
        }

        @TypeConverter
        @JvmStatic
        fun pricesToStringConverter(prices: Prices): String {

            return "Student: ${prices.students}€, Employee: ${prices.employees}€, Others: ${prices.others}€"
        }

        @TypeConverter
        @JvmStatic
        fun cordinatesToLocationDataConverter(coordinate: List<Double>): String {
            if (coordinate.count() < 1) {
                return ""
            }
            return coordinate[0].toString() + "," + coordinate[1].toString()
        }

        @TypeConverter
        @JvmStatic
        fun LocationDataToCordinatesConverter(locationString: String): List<Double>? {

            var splited = locationString.split(",")
            return listOf(splited[0].toDouble(), splited[1].toDouble())
        }
    }
}