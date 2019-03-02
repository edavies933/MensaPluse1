package com.example.emmanueldavies.mensapluse1.LocaionManager

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import com.example.emmanueldavies.mensapluse1.data.LocationData

interface ILocationDetector {

    fun getLastKnowLocation(activityContext: Activity): MutableLiveData<LocationData>
}