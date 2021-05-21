package com.example.fleetmanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.R
import com.example.fleetmanager.entities.Truck
import com.example.fleetmanager.ui.garage.GarageFragment

class GarageAdapter(val context: Context, val fragment: GarageFragment): RecyclerView.Adapter<GarageAdapter.GarageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var vehicles_list =  emptyList<Truck>()

    inner class GarageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val brand: TextView = itemView.findViewById(R.id.brand_txt)
        val license_plate: TextView = itemView.findViewById(R.id.license_plate_txt)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarageViewHolder {
        // Criar nova view que define a User Interface do item da lista
        val view = inflater.inflate(R.layout.recycler_truck_line, parent, false)
        return GarageViewHolder(view)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(holder: GarageViewHolder, position: Int) {
        val currentTruck = vehicles_list[position]
        holder.brand.text = holder.itemView.context.getString(R.string.truck_brand, currentTruck.brand)
        holder.license_plate.text =holder.itemView.context.getString(R.string.truck_license, currentTruck.license_plate)
        holder.itemView.setOnClickListener {
            Log.d("aa", "esta aqui um camiao")
        }
    }

    fun setVehicles(vehicles: List<Truck>){
        this.vehicles_list = vehicles
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return vehicles_list.size
    }
}