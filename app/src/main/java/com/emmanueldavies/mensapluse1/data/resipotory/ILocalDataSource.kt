package com.emmanueldavies.newMensaplus.resipotory

import com.emmanueldavies.mensapluse1.data.Canteen
import com.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe


interface ILocalDataSource {

    fun  queryForMealsByCanteenId (canteenId :Int, date:String) : Maybe<List<Meal>>
    fun saveMealsToDataBase(meals: List<Meal>)
    fun queryForCanteensWithCity(cityName: String): Maybe<List<Canteen>>
    fun saveCanteensToDataBase(canteens: List<Canteen>)
}





