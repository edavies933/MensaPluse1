package com.example.emmanueldavies.newMensaplus.resipotory

import android.arch.lifecycle.LiveData
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData


interface IDataSource<T> {

    val canteenData: LiveData<T>

}


interface IMensaRepository {

    val canteenData: LiveData<List<Canteen>>
    fun getCanteenDataWithCoordinates(locationData: LocationData)
}


