package com.example.emmanueldavies.newMensaplus.resipotory

import com.example.emmanueldavies.mensapluse1.api.MensaAPIInterface
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.resipotory.IRemoteDataSource
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers

class RemoteDataSource(private var apiInterface: MensaAPIInterface) :
    IRemoteDataSource {

    override fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>{
        //    @GET("canteens?near[lat]=52.393535&near[lng]=13.127814")
        return apiInterface.getCanteens(locationData.Latitude,locationData.Longitude)
            .subscribeOn(Schedulers.io())    }

    override fun  queryForMealsByCanteenId (canteenId : Int, date: String) : Maybe<List<Meal>> {
     return   apiInterface.getMeals(canteenId,date).subscribeOn(Schedulers.io())
    }

}





















//    fun queryForMealsByCanteenId(canteenId: Int): MutableLiveData<List<Meal>> {
//        apiInterface.getMeals(1, "2019-1-14").enqueue(object :
//            Callback<List<Meal>> {
//            override fun onResponse(call: Call<List<Meal>>, response: Response<List<Meal>>) {
//                mMealsApiData.setValue(response.body())
//            }
//
//            override fun onFailure(call: Call<List<Meal>>, t: Throwable) {
//
//            }
//        })
//
//        return mMealsApiData
//    }

