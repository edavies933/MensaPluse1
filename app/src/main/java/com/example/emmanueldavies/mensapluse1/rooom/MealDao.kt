package com.example.emmanueldavies.mensapluse1.rooom

import android.arch.persistence.room.*
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe

interface  MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(meal: Meal)

    @Update
    fun update(meal: Meal)

    @Delete
    fun delete (meal: Meal)

    @Query("SELECT * FROM meal_table")
    fun getAllMeals(meal: Meal) : Maybe<Meal>

    @Insert
    fun insertAll(users: List<Meal>)


}