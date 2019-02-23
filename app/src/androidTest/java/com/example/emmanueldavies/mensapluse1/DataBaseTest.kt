package com.example.emmanueldavies.mensapluse1

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.data.Prices
import com.example.emmanueldavies.mensapluse1.rooom.MealDatabase
import io.reactivex.internal.operators.maybe.MaybeObserveOn
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

open class MealDaoTest {
    private lateinit var mealDaoDatabase: MealDatabase
    private lateinit var meal: Meal
    private lateinit var observer: MaybeObserveOn<in Meal>
    lateinit var mealList : List<Meal>
    @Before
    fun initDb() {

        val string = "January 2, 2010"
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val localDate = LocalDate.parse(string, formatter)
        println(localDate) // 2010-01-02

        var prices = Prices("1.2", "3.4", "4.00", "5.32")

        var note: List<String> = listOf("sugar", "milk", "coffee")

         mealList = listOf<Meal>(

            Meal(name = "meal 1a", canteenId = 1, date = "01.01.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 1b", canteenId = 1, date = "01.01.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 2a", canteenId = 2, date = "02.11.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 2b", canteenId = 2, date = "02.11.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 2c", canteenId = 2, date = "02.11.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 3a", canteenId = 3, date = "01.01.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 3b", canteenId = 3, date = "11.01.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 3c", canteenId = 3, date = "11.01.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = null),
            Meal(name = "meal 4a", canteenId = 4, date = "1.2.2019", prices = prices, notes = note,category = "",id = 0,mealPrimaryKey = 0)
        )


        mealDaoDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), MealDatabase::class.java)
            .build()
        for (meal in mealList) {

            mealDaoDatabase.mealDatabaseDao().insert(meal)
        }

    }

    @After
    fun closeDb() {
        mealDaoDatabase.close()
    }

    @Test
    fun getAllMealInDatabase() {

        mealDaoDatabase.mealDatabaseDao().getAllMeal().subscribe {

            assertEquals(
                "count of items in database should be the same as count of items in list of meal",
                mealList.count(),
                it.count()
            )
        }
    }

    @Test
    fun getMealsWithCanteenIdAndDate() {

        mealDaoDatabase.mealDatabaseDao().getMealsWithCanteenIdAndDate(1, "01.01.2019").subscribe {

            assertEquals("but canteen id should be equal", 2, it.count())
        }

    }
}