package com.example.emmanueldavies.mensapluse1

import android.app.Application
import android.location.Geocoder
import java.util.*
import javax.inject.Inject


class CityNameGeoCoder @Inject constructor(context: Application) {

    private val geoCoder = Geocoder(context, Locale.getDefault())

    fun convertLatLonToCityName(lat: Double, lon: Double): String? {
        var address = geoCoder.getFromLocation(lat, lon, 1)
        if (address.count() > 0) {
            return address[0]?.locality

        }
        return null
    }
}