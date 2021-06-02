package com.example.fleetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager

class ManagementMenu : AppCompatActivity() {


    private lateinit var toolbar: androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management_dashboard)

        toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_management_menu)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    fun addVehicle(view: View) {
        val i = Intent(this@ManagementMenu, VehicleRegistration::class.java)
        startActivity(i)
        finish()
    }

    fun addEmployee(view: View) {}
    fun addInspection(view: View) {}
    fun addInsurance(view: View) {}
    fun manageVehicles(view: View) {}
    fun addRevision(view: View) {}
}