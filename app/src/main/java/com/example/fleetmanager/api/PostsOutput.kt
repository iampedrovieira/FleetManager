package com.example.fleetmanager.api

import java.time.LocalDate
import java.util.*

data class OutputLogin(
    val status: Boolean,
    val userid: String,
    val companyid: String,
)

data class OutputVehicle(
    val license_plate: String,
    val brand: String,
    val model: String,
    val total_km: Float,
    val avg_consumption: Float,
    val isActive: Boolean,
    val vehicle_latitude: Float,
    val vehicle_longitude: Float,
    val image: String,
    val vehicle_type: String,
    val company_key: String,
    val fuel_type: String
    )

data class OutputEmployee(
    val employee_key: String,
    val employee_name: String,
    val phone_number: String,
    val employee_address: String,
    val picture: String,
    val company_key: String,
    val on_service: Boolean
    )

data class OutputTrip(
    val trip_id: Int,
    val travel_distance: Float,
    val date: Date,
    val license_plate: String,
    val employee_key: String
)
