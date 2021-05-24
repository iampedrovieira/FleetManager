package com.example.fleetmanager.uiEmployee.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fleetmanager.R

class ProfileEmployeeFragment : Fragment() {

    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile_employee, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_profile)
        return root
    }
}