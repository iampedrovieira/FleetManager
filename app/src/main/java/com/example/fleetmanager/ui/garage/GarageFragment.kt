package com.example.fleetmanager.ui.garage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.R
import com.example.fleetmanager.adapters.GarageAdapter
import com.example.fleetmanager.entities.Truck

class GarageFragment : Fragment() {

    private lateinit var toolbar : androidx.appcompat.widget.Toolbar

    private val camioes = listOf(
            Truck("mercedes", "lll"),
            Truck("bmw", "xxx"),
            Truck("fff", "fff")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_garage, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_garage)

        val camioes = listOf(
                Truck("mercedes", "lll"),
                Truck("bmw", "xxx")
        )

        // Recycler View
        val truck_recycler = root.findViewById<RecyclerView>(R.id.truck_recycler)
        val truck_adapter = GarageAdapter(this.requireContext(), this)
        truck_recycler.adapter = truck_adapter
        truck_recycler.layoutManager = LinearLayoutManager(this.context)

        //val textView: TextView = root.findViewById(R.id.text_garage)

        // View Model
        garageViewModel = ViewModelProvider(requireActivity()).get(GarageViewModel::class.java)
        garageViewModel.allVehicles.observe(viewLifecycleOwner, Observer {vehicles ->
            vehicles?.let {
                //textView.text = it
                truck_adapter.setVehicles(it)
            }
        })

        return root
    }
}