package com.example.fleetmanager.realtimeLocationAll

import com.example.fleetmanager.realtimeLocation.DriverConstructor

interface FirebaseDriverListener {

    fun onDriverAdded(driver: DriverConstructor)

    fun onDriverRemoved(driver: DriverConstructor)

    fun onDriverUpdated(driver: DriverConstructor)
}