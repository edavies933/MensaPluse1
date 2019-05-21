package com.emmanueldavies.mensapluse1.data.rooom

import android.arch.persistence.room.*
import com.emmanueldavies.mensapluse1.domain.model.Canteen
import io.reactivex.Maybe

@Dao
@TypeConverters(DateTypeConverter::class)
interface  CanteenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(canteen: Canteen)

    @Query("SELECT * FROM canteen_table WHERE city = :cityName ")
    fun getCanteenWithCity(cityName: String): Maybe<List<Canteen>>

    @Query("SELECT * FROM canteen_table")
    fun getAllCanteens () : Maybe<List<Canteen>>


    @Query("DELETE FROM canteen_table")
    fun deleteAllEntries ()
}