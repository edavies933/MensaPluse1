package com.emmanueldavies.mensapluse1.internal.Utility

import io.reactivex.Single

interface  ICityNameGeoCoder {

    fun convertLatLonToCityName(lat: Double, lon: Double): Single<String>?
}