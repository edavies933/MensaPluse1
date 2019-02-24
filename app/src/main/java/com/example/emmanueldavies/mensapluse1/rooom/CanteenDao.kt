package com.example.emmanueldavies.mensapluse1.rooom

import android.arch.persistence.room.*
import com.example.emmanueldavies.mensapluse1.data.Canteen
import io.reactivex.Maybe

@Dao
interface  CanteenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(canteen: Canteen)

    @Query("SELECT * FROM canteen_table WHERE city = :cityName ")
    fun getCanteenWithCity(cityName: String): Maybe<List<Canteen>>

    @Query("SELECT * FROM canteen_table")
    fun getAllCanteens () : Maybe<List<Canteen>>


    @Delete
    fun delete (canteen: Canteen)
}