package com.emmanueldavies.mensapluse1.data.resipotory

import com.emmanueldavies.mensapluse1.domain.model.Canteen
import com.emmanueldavies.mensapluse1.domain.model.LocationData
import com.emmanueldavies.mensapluse1.domain.model.Meal
import io.reactivex.Maybe

interface IRemoteDataSource {

    fun  queryForMealsByCanteenId (canteenId :Int, date:String) : Maybe<List<Meal>>
    fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>
}