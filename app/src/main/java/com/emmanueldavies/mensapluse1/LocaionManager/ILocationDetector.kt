package com.emmanueldavies.mensapluse1.LocaionManager

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import com.emmanueldavies.mensapluse1.data.LocationData

interface ILocationDetector {

    fun getLastKnowLocation(activityContext: Activity): MutableLiveData<LocationData>
    fun stopListeningToLocationUpdate()
    fun checkPermissions(): Boolean
}