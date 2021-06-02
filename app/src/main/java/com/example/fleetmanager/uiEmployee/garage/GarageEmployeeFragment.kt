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
import com.example.fleetmanager.MapsTracksActivity
import com.example.fleetmanager.MapsTracksActivityAll
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
        toolbar.inflateMenu(R.menu.garage_menu)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        // Recycler View
        val truck_recycler = root.findViewById<RecyclerView>(R.id.truck_recycler)
        val truck_adapter = GarageAdapter(this.requireContext(), this)
        truck_recycler.adapter = truck_adapter
        truck_recycler.layoutManager = LinearLayoutManager(this.context)

        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        var company_key = sharedPref.getString(getString(R.string.company), "aaaa")
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val callVehiclePost = request.getVehicles(company_key)

        callVehiclePost.enqueue(object: Callback<List<OutputVehicle>> {
            override fun onResponse(
                call: Call<List<OutputVehicle>>,
                response: Response<List<OutputVehicle>>
            ) {
                if(response.isSuccessful){
                    // Registering the Kotlin module with the ObjectMpper instance
                    //val list: List<OutputVehicle> =
                    Log.d("aa", response.body().toString())
                    truck_adapter.setVehicles(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<OutputVehicle>>, t: Throwable) {
                Log.d("FALHOU", "${t.message}")
            }
        })

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.garage_map_icon -> {
                Log.d("aa", "botao track veiculo")
                val intent = Intent(activity, MapsTracksActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}