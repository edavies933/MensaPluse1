package com.example.emmanueldavies.mensapluse1.rooom

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(MealDatabase::class), version = 1)
abstract class MealDatabase : RoomDatabase() {

     abstract  fun mealDatabase() : MealDatabase

     companion object {
         private var INSTANCE : MealDatabase? = null

         fun getInstance (context: Context): MealDatabase? {

             if (INSTANCE == null) {
                 synchronized(MealDatabase::class){
                     INSTANCE = Room.databaseBuilder(context.applicationContext, MealDatabase::class.java,
                         "meal_database.db")
                         .build()
                 }
             }
             return  INSTANCE
         }

         fun destroyInstance() {
             INSTANCE = null
         }
     }

}