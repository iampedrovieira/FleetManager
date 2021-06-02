package com.example.fleetmanager.realtimeLocation

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseHelper constructor(driverId: String) {
    companion object {
        private const val ONLINE_DRIVERS = "online_drivers"
    }
    private val onlineDriverDatabaseReference: DatabaseReference = FirebaseDatabase
        .getInstance("https://fleetmanagement-4bf81-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference
        .child(ONLINE_DRIVERS)
        .child(driverId)
    init {
        onlineDriverDatabaseReference
            .onDisconnect()
            .removeValue()
    }
    fun updateDriver(driver: Driver) {
        onlineDriverDatabaseReference
            .setValue(driver)
        Log.e("Driver Info", " Updated")
    }
    fun deleteDriver() {
        onlineDriverDatabaseReference
            .removeValue()
    }
}