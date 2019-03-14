package com.example.emmanueldavies.mensapluse1.Utility

import android.app.Application
import android.location.Geocoder
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CityNameGeoCoder @Inject constructor(context: Application) :
    ICityNameGeoCoder {

    private val geoCoder = Geocoder(context, Locale.getDefault())

    override fun convertLatLonToCityName(lat: Double, lon: Double): String? {
        var address = geoCoder.getFromLocation(lat, lon, 1)
        if (address.count() > 0) {
            return address[0]?.locality

        }
        return null
    }
}


