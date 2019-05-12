package com.emmanueldavies.mensapluse1.rooom

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.emmanueldavies.mensapluse1.data.Canteen
import com.emmanueldavies.mensapluse1.data.Meal


@Database(entities = [Meal::class, Canteen::class], version = 5)
@TypeConverters(DateTypeConverter::class)
abstract class MensaDatabase  : RoomDatabase() {

    abstract fun mealDatabaseDao(): MensaDao
    abstract fun canteenDatabaseDao(): CanteenDao
}

