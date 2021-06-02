package com.example.fleetmanager

import android.hardware.SensorAdditionalInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class StartTripActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    // Inputs
    private lateinit var selectedVehicle: TextView
    private lateinit var toLocation: TextView
    private lateinit var additionalInfo: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_trip)

        selectedVehicle = findViewById(R.id.vehicleText)
        toLocation = findViewById(R.id.toText)
        additionalInfo = findViewById(R.id.otherinfoText)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_trips)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    fun startTrip(view: View) {}
}