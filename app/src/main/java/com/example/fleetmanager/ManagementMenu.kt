package com.example.fleetmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ManagementMenu : AppCompatActivity() {


    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private val DASHBOARD_TYPE: String = "Dashboard_Clicked"


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
    }

    fun addEmployee(view: View) {}
    fun addInspection(view: View) {
        val i = Intent(this@ManagementDashboard, RevisionSelectionArea::class.java)
        i.putExtra(DASHBOARD_TYPE, "inspection")
        startActivity(i)
    }
    fun addInsurance(view: View) {
        val i = Intent(this@ManagementDashboard, RevisionSelectionArea::class.java)
        i.putExtra(DASHBOARD_TYPE, "insurance")
        startActivity(i)
    }
    fun manageVehicles(view: View) {}
    fun addRevision(view: View) {
        val i = Intent(this@ManagementDashboard, RevisionSelectionArea::class.java)
        i.putExtra(DASHBOARD_TYPE, "revision")
        startActivity(i)
    }
}