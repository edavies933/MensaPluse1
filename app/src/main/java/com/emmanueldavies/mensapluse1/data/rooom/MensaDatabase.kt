package com.emmanueldavies.mensapluse1.data.rooom

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.emmanueldavies.mensapluse1.domain.model.Canteen
import com.emmanueldavies.mensapluse1.domain.model.Meal


@Database(entities = [Meal::class, Canteen::class], version = 5)
@TypeConverters(DateTypeConverter::class)
abstract class MensaDatabase  : RoomDatabase() {

    abstract fun mealDatabaseDao(): MensaDao
    abstract fun canteenDatabaseDao(): CanteenDao
}

