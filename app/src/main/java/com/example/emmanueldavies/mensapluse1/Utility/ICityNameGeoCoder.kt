package com.example.emmanueldavies.mensapluse1.Utility

import io.reactivex.Single

interface  ICityNameGeoCoder {

    fun convertLatLonToCityName(lat: Double, lon: Double): Single<String>?
}