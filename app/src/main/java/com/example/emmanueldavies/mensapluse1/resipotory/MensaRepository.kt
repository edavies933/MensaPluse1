package com.example.emmanueldavies.newMensaplus.resipotory

import com.example.emmanueldavies.mensapluse1.CityNameGeoCoder
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject


class MensaRepository @Inject constructor(
     var mRemoteDataSource: IRemoteDataSource,
     var mLocalDataSource: ILocalDataSource
) {
//    fun getCanteenDataWithCoordinates(locationData: LocationData): Maybe<List<Canteen>>? {
//        var cityName = geoCoder.convertLatLonToCityName(locationData.Latitude, locationData.Longitude)
//        if (cityName == null){
//
//            return mRemoteDataSource.getCanteenDataWithCoordinates(locationData)
//                .doOnSuccess {
//                    mLocalDataSource.saveCanteensToDataBase(it)
//                }.doOnError {
//
//                    var xx = it
//                }
//
//        }else {
//            return Maybe.concat(mLocalDataSource.queryForCanteensWithCity(cityName).subscribeOn(Schedulers.io()),
//                mRemoteDataSource.getCanteenDataWithCoordinates(locationData).onErrorComplete()
//                    .doOnSuccess {
//                        mLocalDataSource.saveCanteensToDataBase(it)
//
//                    }.doOnError {
//
//                        var xx = it
//                    }).filter { !it.isEmpty() }.firstElement().doOnError {
//
//                var xx = it
//            }
//
//        }
//
//
//
//        //Todo Check if there is internet or not
//    }

    fun getMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>> {
        return Maybe.concat(mLocalDataSource.queryForMealsByCanteenId(canteenId, date).subscribeOn(Schedulers.io()).doOnSuccess {

            var mealsFromDb = it
        },
            mRemoteDataSource.queryForMealsByCanteenId(canteenId, date)
                .doOnSuccess {
                addCanteenIdToMeals(it, canteenId, date)
                mLocalDataSource.saveMealsToDataBase(it)
            }.doOnError {

                var xx = it
            }).filter { !it.isEmpty() }

            .firstElement().doOnError {

                var xx = it
            }

    }

    private fun addCanteenIdToMeals(
        it: List<Meal>,
        canteenId: Int,
        date: String
    ) {
        for (meal in it) {
            meal.canteenId = canteenId
            meal.date = date
        }
    }

}