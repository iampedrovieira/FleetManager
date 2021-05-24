package com.example.fleetmanager.entities

data class Truck(
        val brand: String,
        val license_plate: String,
        val model: String,
        val total_km: Float,
        val avg_consumption: Float,
        val isActive: Boolean,
        val vehicle_latitude: Float,
        val vehicle_longitude: Float,
        val image: String,
        val vehicle_type: String,


)
