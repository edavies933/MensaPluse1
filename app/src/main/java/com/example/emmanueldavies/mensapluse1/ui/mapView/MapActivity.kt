package com.example.emmanueldavies.mensapluse1.ui.mapView

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.emmanueldavies.mensapluse1.LocaionManager.ILocationDetector
import com.example.emmanueldavies.mensapluse1.Utility.MensaAppViewModelFactory
import com.example.emmanueldavies.mensapluse1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import javax.inject.Inject


class MapActivity : AppCompatActivity(), OnMapReadyCallback {


    private var mMap: GoogleMap? = null
    @Inject
    lateinit var mensaAppViewModelFactory: MensaAppViewModelFactory
    lateinit var mapViewModel: MapViewModel
    @Inject
    lateinit var locationDetector: ILocationDetector

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {

        mMap = googleMap

        locationDetector.getLastKnowLocation(this).observe(
            this,
            Observer(function = {
                if (it != null) {
                    mMap?.isMyLocationEnabled = true
                    mMap?.uiSettings?.isMyLocationButtonEnabled = true
                }
            })
        )

        mapViewModel.canteens.observe(this, Observer {
            if (it != null) {
                for ((index, canteen) in it.withIndex()) {

                    if (canteen.coordinates != null) {
                        val canteenMarker = LatLng(canteen.coordinates!![0], canteen.coordinates!![1])
                        var marker: Marker? =
                            mMap?.addMarker(MarkerOptions().position(canteenMarker).title(canteen.name))

                        if (index == it.count() - 1)
                            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(canteenMarker, 12.0f))
//                        marker?.showInfoWindow()
                    }
                }
            }
        })

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.emmanueldavies.mensapluse1.R.layout.activity_map)

        mapToolbar.title = getString(R.string.mapActivityToolBarTitle)

        setSupportActionBar(mapToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mapViewModel = ViewModelProviders.of(this, mensaAppViewModelFactory).get(MapViewModel::class.java)
        locationDetector.getLastKnowLocation(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.example.emmanueldavies.mensapluse1.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationDetector.getLastKnowLocation(this).observe(this, Observer {
            if (it == null) {

//                updateView(ViewState.noLocationFound())


            } else {
                mapViewModel.getCanteens(it)
                locationDetector.stopListeningToLocationUpdate()
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)

    }
}
