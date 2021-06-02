package com.example.fleetmanager

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fleetmanager.uiEmployee.dashboard.DashboardEmployeeFragment
import com.example.fleetmanager.uiEmployee.garage.GarageEmployeeFragment
import com.example.fleetmanager.uiEmployee.profile.ProfileEmployeeFragment
import com.example.fleetmanager.uiEmployee.trips.TripsEmployeeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.fleetmanager.ui.employees.EmployeesFragment

class MainActivityEmployee : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView
    private var isPlay: Boolean = true
    private lateinit var playStopTrip : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_employee)

        playStopTrip = findViewById(R.id.playStopTrip)

        val TAG = "MainActivityEmployee"

        val garageFragment = GarageEmployeeFragment()
        val dashboardFragment = DashboardEmployeeFragment()
        val profileFragment = ProfileEmployeeFragment()
        val tripsFragment = TripsEmployeeFragment()

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationViewEmployee)
        bottomNavigationView.background = null
        setCurrentFragment(dashboardFragment)



        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_dashboard_employee -> {
                    setCurrentFragment(dashboardFragment)
                    Log.i("aa", "Home Selected")
                    badgeClear(R.id.navigation_dashboard_employee)
                }
                R.id.navigation_garage_employee -> {
                    setCurrentFragment(garageFragment)
                    Log.i(TAG, "garage Selected")
                    badgeClear(R.id.navigation_garage_employee)
                }
                R.id.navigation_trips_employee -> {
                    setCurrentFragment(tripsFragment)
                    Log.i(TAG, "trips Selected")
                    badgeClear(R.id.navigation_trips_employee)
                }
                R.id.navigation_profile_employee -> {
                    setCurrentFragment(profileFragment)
                    Log.i(TAG, "profile Selected")
                    badgeClear(R.id.navigation_profile_employee)
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
            replace(R.id.fl_wrapper_employee, fragment)
            commit()
        }


    fun playStopTrip(view: View) {
        Log.d("playButtonStatus", "Entrada: " + isPlay.toString())
        if(isPlay){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_stop, theme))
            } else {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_stop))
            }
            isPlay=false
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_play, theme))
            } else {
                playStopTrip.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
            }
            isPlay=true
        }
        Log.d("playButtonStatus", "Saída: " + isPlay.toString())
    }

    fun replaceFragmentChat(){
        var bundle = Bundle()
        val chat_fragment = EmployeesFragment()
        bundle.putBoolean("admin",false)
        chat_fragment.arguments = bundle
        setCurrentFragment(chat_fragment)
        Log.i("aa", "Home Selected")
    }

}