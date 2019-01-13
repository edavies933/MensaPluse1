package com.example.emmanueldavies.newMensaplus.resipotory

import android.arch.lifecycle.LiveData
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.data.Meal

import javax.inject.Inject


class MensaRepository  @Inject constructor  (var mRemoteDataSource: RemoteDataSource, var mLocalDataSource: LocalDataSource) :
    IMensaRepository {

    override val canteenData: LiveData<List<Canteen>>
        get() = mRemoteDataSource.canteenData

    val meals: LiveData<List<Meal>>
        get() = mRemoteDataSource.meals

    override fun getCanteenDataWithCoordinates(locationData: LocationData) {

        mRemoteDataSource.getCanteenDataWithCoordinates(locationData)
    }

    fun getMealsByCanteenId(canteenId: Int) {

        mRemoteDataSource.queryForMealsByCanteenId(canteenId)
    }
}