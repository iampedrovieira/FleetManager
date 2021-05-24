package com.example.fleetmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

private lateinit var brandText: TextView
private lateinit var modelText: TextView
private lateinit var plateText: TextView
private lateinit var kilometersText: TextView
private lateinit var consumptionText: TextView
private lateinit var lastRevisionText: TextView
private lateinit var lastInsuranceText: TextView
private lateinit var vehicleBrandText: TextView
private lateinit var vehicleEmployeeText: TextView
private lateinit var vehicleVinText: TextView

class VehicleDescription : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_description)

        brandText = findViewById(R.id.brand_text)
        modelText = findViewById(R.id.model_text)
        plateText = findViewById(R.id.plate_text)
        kilometersText = findViewById(R.id.kilometers_text)
        consumptionText = findViewById(R.id.consumption_text)
        lastRevisionText = findViewById(R.id.last_revision_text)
        lastInsuranceText = findViewById(R.id.insurance_date_text)
        vehicleBrandText = findViewById(R.id.vehicle_brand_name)
        vehicleEmployeeText = findViewById(R.id.vehicle_name)
        vehicleVinText = findViewById(R.id.vehicle_vin)
    }
}