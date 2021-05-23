package com.example.fleetmanager.api

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
