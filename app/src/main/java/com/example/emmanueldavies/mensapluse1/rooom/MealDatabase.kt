package com.example.emmanueldavies.mensapluse1.rooom

import android.app.Application
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.emmanueldavies.mensapluse1.data.Meal


@Database(entities = [Meal::class], version = 1)
abstract class MealDatabase  : RoomDatabase() {

    abstract fun mealDatabaseDao(): MealDao


    private var mealDatabase: MealDatabase? = null

    fun destroyInstance() {
        mealDatabase = null
    }


}