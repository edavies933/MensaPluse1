package com.example.emmanueldavies.mensapluse1.Utility

import android.app.Application
import android.location.Geocoder
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class CityNameGeoCoder @Inject constructor(
    context: Application
) :
    ICityNameGeoCoder {

    private val geoCoder = Geocoder(context, Locale.getDefault())

    override fun convertLatLonToCityName(lat: Double, lon: Double): Single<String>? {
        var cityName: String? = null

        return Single.fromCallable {

            try {
                var address = geoCoder.getFromLocation(lat, lon, 1)
                if (address.count() > 0) {
                    cityName = address[0]?.locality
                    return@fromCallable cityName
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }

        }


    }


}




