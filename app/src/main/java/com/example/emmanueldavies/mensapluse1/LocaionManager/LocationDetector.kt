package com.example.emmanueldavies.mensapluse1.LocaionManager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.example.emmanueldavies.mensapluse1.R
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class LocationDetector @Inject constructor() : ILocationDetector {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    lateinit var activity: Activity

    var locationLifeData: MutableLiveData<LocationData> = MutableLiveData()

    override fun getLastKnowLocation(activityContext: Activity): MutableLiveData<LocationData> {

        activity = activityContext
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activityContext)

        if (!checkPermissions()) {
            requestPermissions()
            getLastLocation()


        } else {
            getLastLocation()
        }

        return locationLifeData
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    fun getLastLocation() {

        fusedLocationClient.lastLocation
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful && task.result != null) {

                    var locationData = LocationData(task.result.latitude, task.result.longitude)
                    task.result.latitude

                    locationLifeData.postValue(locationData)
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    (activity as MainActivity).viewSnap. showSnackBar( activity,R.string.no_location_detected)
                    locationLifeData.postValue(null)
                }
            }

    }


    /**
     * Return the current mainActivityState of the permissions needed.
     */
    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            Log.i(TAG, "Displaying permission rationale to provide additional activityContext.")
            (activity as MainActivity).viewSnap. showSnackBar(activity, R.string.permission_rationale, android.R.string.ok, View.OnClickListener {
                // Request permission
                startLocationPermissionRequest()
            })

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given mainActivityState or the user denied the permission
            // previously and checked "Never ask again".
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

}

