package com.example.emmanueldavies.newMensaplus.resipotory

import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MensaRepository @Inject constructor(
    private var mRemoteDataSource: IRemoteDataSource,
    private var mLocalDataSource: ILocalDataSource
) {
    fun getCanteenDataWithCoordinates(locationData: LocationData): Maybe<List<Canteen>> {
        return mRemoteDataSource.getCanteenDataWithCoordinates(locationData).onErrorComplete()//Todo Check if there is internet or not
    }

    fun getMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>> {
        return Maybe.concat(mLocalDataSource.queryForMealsByCanteenId(canteenId, date).subscribeOn(Schedulers.io()),
            mRemoteDataSource.queryForMealsByCanteenId(canteenId, date).doOnSuccess {
                addCanteenIdToMeals(it, canteenId, date)
                mLocalDataSource.saveMealsToDataBase(it)
            }).filter { !it.isEmpty() }

            .firstElement()

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