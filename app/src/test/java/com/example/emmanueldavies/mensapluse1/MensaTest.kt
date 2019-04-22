package com.example.emmanueldavies.mensapluse1

import com.example.emmanueldavies.mensapluse1.data.Prices
import com.example.emmanueldavies.mensapluse1.rooom.DateTypeConverter
import org.junit.Assert
import org.junit.Test

class MensaTest {

    @Test
    fun testListOfStringToSingleStringConverter() {


        var singleString =  DateTypeConverter.notesToStringConverter(listOf("a", "b", "c"))

        Assert.assertEquals("string is not properly formatted", "a, b, c", singleString)

    }

    @Test
    fun singleStringToTestListOfStringConverter() {


        var singleString =  DateTypeConverter.stringToNoteConverter("a, b, c")

        Assert.assertEquals("string is not properly formatted", "a", singleString[0])
        Assert.assertEquals("string is not properly formatted", "b", singleString[1])
        Assert.assertEquals("string is not properly formatted", "c", singleString[2])

    }

    @Test
    fun testStringToPriceConverter_ShouldConvertASingleStringToAPriceObject() {

        var singleString = "Student: 2.22€, Employee: 3.33€, Others: 4.44€"
        var convertedPrices =  DateTypeConverter.stringToPricesConvert(singleString)

        Assert.assertEquals("2.22", convertedPrices.students)
        Assert.assertEquals("3.33", convertedPrices.employees)
        Assert.assertEquals("4.44", convertedPrices.others)

    }

    @Test
    fun testPricesToStringConverter_ShouldCovertPriceObjectToASingleString(){

        var prices = Prices("2.22","3.33", others = "4.44")
        var convertedPrices =  DateTypeConverter.pricesToStringConverter(prices)

        Assert.assertEquals("Student: 2.22€, Employee: 3.33€, Others: 4.44€", convertedPrices)

    }
}