package com.example.fleetmanager.uiEmployee.trips

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.R
import com.example.fleetmanager.adapters.TripsAdapter
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputTrip
import com.example.fleetmanager.api.OutputVehicle
import com.example.fleetmanager.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripsEmployeeFragment : Fragment() {
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_trips_employee, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_trips)

        // Recycler View
        val trip_recycler = root.findViewById<RecyclerView>(R.id.trips_recycler)
        val trip_adapter = TripsAdapter(this.requireContext(), this)
        trip_recycler.adapter = trip_adapter
        trip_recycler.layoutManager = LinearLayoutManager(this.context)

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        var user_key = sharedPref.getString(getString(R.string.uid), "aaaa")
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val callTripPost = request.getTrips(user_key)

        callTripPost.enqueue(object: Callback<List<OutputTrip>> {
            override fun onResponse(
                call: Call<List<OutputTrip>>,
                response: Response<List<OutputTrip>>
            ) {
                if(response.isSuccessful){
                    // Registering the Kotlin module with the ObjectMpper instance
                    //val list: List<OutputVehicle> =
                    Log.d("aa", response.body().toString())
                    trip_adapter.setTrips(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<OutputTrip>>, t: Throwable) {
                Log.d("FALHOU", "${t.message}")
            }
        })

        return root
    }
}