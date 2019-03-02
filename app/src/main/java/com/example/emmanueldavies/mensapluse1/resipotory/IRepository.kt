package com.example.emmanueldavies.mensapluse1.resipotory

import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe

interface IRepository {
    fun getCanteenNames(locationData: LocationData, cityName: String): Maybe<List<Canteen>>
    fun getMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>>
}