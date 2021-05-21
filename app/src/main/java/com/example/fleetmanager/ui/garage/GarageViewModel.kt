package com.example.fleetmanager.ui.garage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fleetmanager.entities.Truck

class GarageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is garage Fragment"
    }
    val text: LiveData<String> = _text


    val allVehicles: LiveData<List<Truck>>
    val cam_ = MutableLiveData<List<Truck>>()
    init {
        cam_.apply {
            val trucks = ArrayList<Truck>()
            trucks.add(Truck("merdeces", "xx-xx-xx"))
            trucks.add(Truck("toyota", "xx-ll-aa"))
            value = trucks
        }
        allVehicles = cam_
        Log.d("aa", allVehicles.toString())
    }

    //allVehicles = cam_

}