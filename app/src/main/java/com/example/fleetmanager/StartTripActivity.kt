package com.example.fleetmanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.SensorAdditionalInfo
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.fleetmanager.uiEmployee.dashboard.DashboardEmployeeFragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main_employee.*

class StartTripActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    // Inputs
    private lateinit var selectedVehicle: TextView
    private lateinit var toLocation: TextView
    private lateinit var additionalInfo: TextView
    private lateinit var start_btn: Button
    private lateinit var lastLocation : Location
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback : LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_trip)

        selectedVehicle = findViewById(R.id.vehicleText)
        toLocation = findViewById(R.id.toText)
        additionalInfo = findViewById(R.id.otherinfoText)
        start_btn = findViewById(R.id.start)


        val sharedPref: SharedPreferences =
            getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

        start_btn.setOnClickListener {

            val destination = toLocation.text.toString()
            val origin_lat = location.latitude
            val origin_lng = location.longitude

           val intent = Intent(this, MainActivityEmployee::class.java).apply {
               putExtra("DESTINATION", destination)
               putExtra("ORI_LAT", origin_lat)
               putExtra("ORI_LNG", origin_lng)
           }

            with(sharedPref.edit()) {
                putBoolean("active", true)
                commit()
            }
            startActivity(intent)
            finish()
 }


        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_trips)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0!!.lastLocation
                location = LatLng(lastLocation.latitude, lastLocation.longitude)
            }
        }

        createLocationRequest()

    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                com.example.fleetmanager.uiEmployee.dashboard.LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }
}

