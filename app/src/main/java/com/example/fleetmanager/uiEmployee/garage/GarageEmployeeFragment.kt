package com.example.fleetmanager.uiEmployee.garage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.R
import com.example.fleetmanager.adapters.GarageAdapter
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputVehicle
import com.example.fleetmanager.api.ServiceBuilder
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GarageEmployeeFragment : Fragment() {
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_garage_employee, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_garage)

        return root
    }
}