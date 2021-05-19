package com.example.fleetmanager.ui.employees

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmployeesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is employees Fragment"
    }
    val text: LiveData<String> = _text
}