package com.emmanueldavies.mensapluse1.data.rooom

import android.arch.persistence.room.*
import com.emmanueldavies.mensapluse1.domain.model.Meal
import io.reactivex.Maybe

@Dao
@TypeConverters(DateTypeConverter::class)
interface  MensaDao {

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

    @Query("DELETE FROM meal_table WHERE CanteenId = :canteenId AND Date =  :canteenDate")
    fun deleteMealOnDate (canteenId: Int, canteenDate: String )
}


