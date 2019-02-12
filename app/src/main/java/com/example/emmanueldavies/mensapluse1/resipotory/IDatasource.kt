package com.example.emmanueldavies.newMensaplus.resipotory

import android.arch.lifecycle.LiveData
import com.example.emmanueldavies.mensapluse1.data.Canteen
import com.example.emmanueldavies.mensapluse1.data.LocationData
import io.reactivex.Maybe


interface IDataSource<T> {


}


interface IMensaRepository {

    val canteenData: LiveData<List<Canteen>>
    fun getCanteenDataWithCoordinates(locationData: LocationData) : Maybe<List<Canteen>>
}


