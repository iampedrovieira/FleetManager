package com.example.fleetmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fleetmanager.ui.dashboard.DashboardFragment
import com.example.fleetmanager.ui.employees.EmployeesFragment
import com.example.fleetmanager.ui.garage.GarageFragment
import com.example.fleetmanager.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val TAG = "MainActivity"

        val garageFragment = GarageFragment()
        val dashboardFragment = DashboardFragment()
        val profileFragment = ProfileFragment()
        val employeesFragment = EmployeesFragment()
        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        setCurrentFragment(dashboardFragment)



        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_dashboard -> {
                    setCurrentFragment(dashboardFragment)
                    Log.i(TAG, "Home Selected")
                    badgeClear(R.id.navigation_dashboard)
                }
                R.id.navigation_garage -> {
                    setCurrentFragment(garageFragment)
                    Log.i(TAG, "garage Selected")
                    badgeClear(R.id.navigation_garage)
                }
                R.id.navigation_employees -> {
                    var bundle = Bundle()
                    bundle.putBoolean("admin",true)
                    employeesFragment.arguments = bundle
                    setCurrentFragment(employeesFragment)
                    Log.i(TAG, "employees Selected")
                    badgeClear(R.id.navigation_employees)
                }
                R.id.navigation_profile -> {
                    setCurrentFragment(profileFragment)
                    Log.i(TAG, "profile Selected")
                    badgeClear(R.id.navigation_profile)
                }
            }
            true
        }



    }


    private fun badgeClear(id: Int) {
        val badgeDrawable = bottomNavigationView.getBadge(id)
        if (badgeDrawable != null) {
            badgeDrawable.isVisible = false
            badgeDrawable.clearNumber()
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    fun openManagementDashboard(view: View) {
        val intent = Intent(this, ManagementDashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        startActivity(intent)
    }

}