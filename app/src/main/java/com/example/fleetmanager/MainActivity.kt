package com.example.fleetmanager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
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

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()


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
}