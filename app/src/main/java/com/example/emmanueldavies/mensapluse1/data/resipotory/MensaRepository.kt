package com.example.emmanueldavies.newMensaplus.resipotory

import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal
import com.example.emmanueldavies.mensapluse1.data.resipotory.IRemoteDataSource
import com.example.emmanueldavies.mensapluse1.data.resipotory.IRepository
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MensaRepository(
    var mRemoteDataSource: IRemoteDataSource,
    var mLocalDataSource: ILocalDataSource
) : IRepository {


    override fun getCanteens(locationData: LocationData): Maybe<List<Canteen>> {

        return Maybe.concat(
            // get items from db first
            mLocalDataSource.queryForCanteensWithCity(locationData.cityName).subscribeOn(Schedulers.io()).doOnSuccess {
            },
            mRemoteDataSource.getCanteenDataWithCoordinates(locationData).subscribeOn(Schedulers.io()).doOnSuccess {
                mLocalDataSource.saveCanteensToDataBase(it)
            }
        ).filter { !it.isEmpty() }.firstElement()
    }

    override fun getMealsByCanteenId(canteenId: Int, date: String): Maybe<List<Meal>> {
        return Maybe.concat(mLocalDataSource.queryForMealsByCanteenId(
            canteenId,
            date
        ).subscribeOn(Schedulers.io()),

            mRemoteDataSource.queryForMealsByCanteenId(canteenId, date)
                .doOnSuccess {
                    addCanteenIdToMeals(it, canteenId, date)
                    mLocalDataSource.saveMealsToDataBase(it)
                }).debounce(5000, TimeUnit.MILLISECONDS).firstElement().filter { !it.isEmpty() }
    }

    override fun getMealDirectlyFromDb(canteenId: Int, date: String): Maybe<List<Meal>> {
        return mLocalDataSource.queryForMealsByCanteenId(canteenId, date)
    }

    fun addCanteenIdToMeals(
        it: List<Meal>,
        canteenId: Int,
        date: String
    ) {
        for (meal in it) {
            meal.canteenId = canteenId
            meal.date = date
        }
        mLocalDataSource.saveMealsToDataBase(it)
    }



}