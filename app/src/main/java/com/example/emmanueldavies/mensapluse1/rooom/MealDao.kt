package com.example.emmanueldavies.mensapluse1.rooom

import android.arch.persistence.room.*
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe
import java.time.LocalDate
import java.util.*

@Dao
interface  MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListOfMeal(meal: List<Meal>)

    @Update
    fun update(meal: Meal)

    @Delete
    fun delete (meal: Meal)

    @Query("SELECT * FROM meal_table WHERE CanteenId = :canteenId AND Date =  :canteenDate")
    fun getMealsWithCanteenIdAndDate(canteenId: Int, canteenDate: String): Maybe<List<Meal>>

    @Query("SELECT * FROM meal_table")
    fun getAllMeal () : Maybe<List<Meal>>


}

//@Dao
//interface  CanteenDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(canteen: Canteen)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertListOfMeal(canteen: List<Canteen>)
//
//    @Update
//    fun update(canteen: Canteen)
//
//    @Delete
//    fun delete (canteen: Canteen)
//
//    @Query("SELECT * FROM canteen_table WHERE Id = :canteenId ")
//    fun getCanteenWithCanteenId(canteenId: Int): Maybe<List<Canteen>>
//
//    @Query("SELECT * FROM canteen_table")
//    fun getAllCanteens () : Maybe<List<Canteen>>
//
//
//}