package com.example.fleetmanager.uiEmployee.dashboard

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fleetmanager.MainActivity
import com.example.fleetmanager.R

class DashboardEmployeeFragment : Fragment() {

    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard_employee, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_dashboard)

        return root
    }
}