package com.example.fleetmanager.ui.employees

import android.content.Context
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
import com.example.fleetmanager.MainActivity
import com.example.fleetmanager.MainActivityEmployee
import com.example.fleetmanager.R
import com.example.fleetmanager.adapters.EmployeesAdapter
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputEmployee
import com.example.fleetmanager.api.OutputVehicle
import com.example.fleetmanager.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class EmployeesFragment : Fragment() {

    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private var isAdmin =false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employees, container, false)

        toolbar = root.findViewById(R.id.toolbar)

        val bundle = arguments
        isAdmin = bundle?.get("admin") as Boolean
        if(isAdmin){
            //from adminActiviy
            toolbar.title = getString(R.string.title_employees)

            // Recycler View
            val employee_recycler = root.findViewById<RecyclerView>(R.id.employees_recycler)
            val employee_adapter = EmployeesAdapter(this.requireContext(), this)
            employee_recycler.adapter = employee_adapter
            employee_recycler.layoutManager = LinearLayoutManager(this.context)

            val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

            var company_key = sharedPref.getString(getString(R.string.company), "aaaa")
            val request = ServiceBuilder.buildService(Endpoints::class.java)
            val callEmployeePost = request.getEmployees(company_key)

            callEmployeePost.enqueue(object: Callback<List<OutputEmployee>> {
                override fun onResponse(
                    call: Call<List<OutputEmployee>>,
                    response: Response<List<OutputEmployee>>
                ) {
                    if(response.isSuccessful){
                        // Registering the Kotlin module with the ObjectMpper instance
                        //val list: List<OutputVehicle> =
                        Log.d("aa", response.body().toString())
                        employee_adapter.setEmployees(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<OutputEmployee>>, t: Throwable) {
                    Log.d("FALHOU", "${t.message}")
                }
            })
        }else{
            toolbar.title = getString(R.string.chat)
            //MUDAR ISTO PARA O NOVO ENDPOINT

            // Recycler View
            val employee_recycler = root.findViewById<RecyclerView>(R.id.employees_recycler)
            val employee_adapter = EmployeesAdapter(this.requireContext(), this)
            employee_recycler.adapter = employee_adapter
            employee_recycler.layoutManager = LinearLayoutManager(this.context)

            val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )

            var company_key = sharedPref.getString(getString(R.string.company), "aaaa")
            val request = ServiceBuilder.buildService(Endpoints::class.java)
            val callEmployeePost = request.getEmployees(company_key)

            callEmployeePost.enqueue(object: Callback<List<OutputEmployee>> {
                override fun onResponse(
                    call: Call<List<OutputEmployee>>,
                    response: Response<List<OutputEmployee>>
                ) {
                    if(response.isSuccessful){
                        // Registering the Kotlin module with the ObjectMpper instance
                        //val list: List<OutputVehicle> =
                        Log.d("aa", response.body().toString())
                        employee_adapter.setEmployees(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<List<OutputEmployee>>, t: Throwable) {
                    Log.d("FALHOU", "${t.message}")
                }
            })
        }


        return root
    }



}