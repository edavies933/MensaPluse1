package com.example.emmanueldavies.newMensaplus.resipotory

import android.arch.lifecycle.LiveData
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe
import io.reactivex.Observable


interface ILocalDataSource {

    fun  queryForMealsByCanteenId (canteenId :Int, date:String) : Maybe<List<Meal>>
    fun saveMealsToDataBase(meals: List<Meal>)
    fun queryForCanteensWithCity(cityName: String): Maybe<List<Canteen>>
    fun saveCanteensToDataBase(canteens: List<Canteen>)
}






