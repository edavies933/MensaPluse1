package com.example.emmanueldavies.newMensaplus.resipotory

import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe
import java.util.*
import java.util.logging.Logger

import javax.inject.Inject


class MensaRepository  @Inject constructor  (var mRemoteDataSource: RemoteDataSource, var mLocalDataSource: LocalDataSource)
     {

         val Log = Logger.getLogger(MensaRepository::class.java.name)


     fun getCanteenDataWithCoordinates(locationData: LocationData): Maybe<List<Canteen>> {

       return mRemoteDataSource.getCanteenDataWithCoordinates(locationData)

    }

    fun getMealsByCanteenId(canteenId: Int, date: String) : Maybe<List<Meal>>{

    var meals =     mLocalDataSource.getMealFromDb(canteenId, date).subscribe(
        {var meals = it

        },
        {
            var errror = it.localizedMessage
        }
    )


       return  mRemoteDataSource.queryForMealsByCanteenId(canteenId,date)
    }
}