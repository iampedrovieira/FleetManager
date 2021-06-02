package com.example.fleetmanager.realtimeLocation

@FunctionalInterface
interface IPositiveNegativeListener {

    fun onPositive()

    fun onNegative() {

    }
}