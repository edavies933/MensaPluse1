package com.emmanueldavies.newMensaplus.resipotory

import com.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.emmanueldavies.mensapluse1.data.Canteen
import com.emmanueldavies.mensapluse1.data.LocationData
import com.emmanueldavies.mensapluse1.data.Meal
import com.emmanueldavies.mensapluse1.data.resipotory.IRemoteDataSource
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class RemoteDataSource(private var apiInterface: MensaAPIInterface) :
    IRemoteDataSource {

    override fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>{
        //    @GET("canteens?near[lat]=52.393535&near[lng]=13.127814")
        return apiInterface.getCanteens(locationData.Latitude,locationData.Longitude,10)
            .subscribeOn(Schedulers.io())    }

    override fun  queryForMealsByCanteenId (canteenId : Int, date: String) : Maybe<List<Meal>> {
     return   apiInterface.getMeals(canteenId,date).subscribeOn(Schedulers.io())
    }

}
