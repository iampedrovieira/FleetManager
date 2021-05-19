package com.example.fleetmanager.ui.garage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GarageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is garage Fragment"
    }
    val text: LiveData<String> = _text
}