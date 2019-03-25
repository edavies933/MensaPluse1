package com.example.emmanueldavies.mensapluse1.LocaionManager
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import com.example.emmanueldavies.mensapluse1.data.LocationData
import com.example.emmanueldavies.mensapluse1.ui.MainActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDetector @Inject constructor() : ILocationDetector, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private val TAG = LocationDetector::class.java.simpleName

    override fun onLocationChanged(location: Location) {

        locationLifeData.postValue(LocationData(location.latitude, location.longitude))
        stopListeningToLocationUpdate()

    }

    override fun onConnected(p0: Bundle?) {
        Log.i(TAG, "google api connected.")
        requestLocationUpdates()


    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i(TAG, "google api connection failed.")

    }


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val UPDATE_INTERVAL = (5 * 1000).toLong()
    private val FASTEST_UPDATE_INTERVAL = (3 * 1000).toLong()
    private val MAX_WAIT_TIME = (10 * 1000).toLong()
    private var mLocationRequest: LocationRequest? = null


    private var mGoogleApiClient: GoogleApiClient? = null

    lateinit var activity: Activity

    var locationLifeData: MutableLiveData<LocationData> = MutableLiveData()

    override fun getLastKnowLocation(activityContext: Activity): MutableLiveData<LocationData> {

        activity = activityContext
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activityContext)

        if (!checkPermissions()) {
            requestPermissions()
        }
        getLastLocation()

        return locationLifeData
    }

    private fun buildGoogleApiClient(activity: Activity) {
        if (mGoogleApiClient != null) {
            if (!mGoogleApiClient!!.isConnected) {
                mGoogleApiClient?.connect()
            }
            return
        }
        mGoogleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()
        createLocationRequest()
        mGoogleApiClient?.connect()

    }

    fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()?.apply {
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = MAX_WAIT_TIME
        }

    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {

        fusedLocationClient.lastLocation
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful && task.result != null) {

                    var locationData = LocationData(task.result.latitude, task.result.longitude)
                    task.result.latitude

                    locationLifeData.postValue(locationData)
                } else {
                    Log.i(TAG, "could not get last know location")
                    buildGoogleApiClient(activity)

                }
            }

    }

    fun requestLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this@LocationDetector
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            Log.i(TAG, "could not request location update $e")

        }

    }

    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        LocationServices.FusedLocationApi.removeLocationUpdates(
            mGoogleApiClient, this
        )
    }

    /**
     * Return the current mainActivityState of the permissions needed.
     */
    override fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

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
            (activity as MainActivity).viewSnap.showSnackBar(
                activity,
                com.example.emmanueldavies.mensapluse1.R.string.permission_rationale,
                android.R.string.ok,
                View.OnClickListener {
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



    override fun stopListeningToLocationUpdate() {
        if (mGoogleApiClient != null) {

            if (mGoogleApiClient?.isConnected!!) {
                removeLocationUpdates()
                mGoogleApiClient?.disconnect()

            }
        }

    }

}

