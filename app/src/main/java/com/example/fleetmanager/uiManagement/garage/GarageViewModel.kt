/*package com.example.fleetmanager.ui.garage

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fleetmanager.R
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputVehicle

class GarageViewModel(application: Application) : AndroidViewModel(application) {

    val allVehicles: LiveData<List<OutputVehicle>>


    //val sharedPref = application.getSharedPreferences(
     //   getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    val sharedPref: SharedPreferences =
        application.getSharedPreferences(
            application.applicationContext.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

    var company_key = sharedPref.getString(application.applicationContext.getString(R.string.company), "aaaa")

    init{


        allVehicles = company_key?.let { getVehicles(it) }!!
    }

}*/