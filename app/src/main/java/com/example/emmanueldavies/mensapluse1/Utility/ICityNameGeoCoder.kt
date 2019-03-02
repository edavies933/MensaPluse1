package com.example.emmanueldavies.mensapluse1.Utility

interface  ICityNameGeoCoder {

    fun convertLatLonToCityName(lat: Double, lon: Double): String?
}