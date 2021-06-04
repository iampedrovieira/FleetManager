package com.example.fleetmanager.realtimeLocation

data class Driver(val lat: Double, val lng: Double, val driverId: String = "0000")

class DriverConstructor constructor() {

    var lat: Double = 0.0
    var lng: Double = 0.0
    var driverId: String = ""

    fun setLat(lat: Double) = apply { this.lat = lat }

    fun setLng(lng: Double) = apply { this.lng = lng }

    fun setDriverId(driverId: String) = apply { this.driverId = driverId }

    fun updateDriver(lat: Double, lng: Double) {
        this.lat = lat
        this.lat = lng
    }
}