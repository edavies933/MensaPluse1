package com.example.emmanueldavies.mensapluse1.resipotory

import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe

interface IRemoteDataSource {

    fun  queryForMealsByCanteenId (canteenId :Int, date:String) : Maybe<List<Meal>>
    fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>
}