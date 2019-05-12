package com.emmanueldavies.mensapluse1.data.resipotory

import com.emmanueldavies.mensapluse1.data.Canteen
import com.emmanueldavies.mensapluse1.data.LocationData
import com.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe

interface IRemoteDataSource {

    fun  queryForMealsByCanteenId (canteenId :Int, date:String) : Maybe<List<Meal>>
    fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>
}