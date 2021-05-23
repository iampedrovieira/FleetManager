package com.example.fleetmanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.R
import com.example.fleetmanager.api.OutputVehicle
import com.example.fleetmanager.ui.garage.GarageFragment

class GarageAdapter(val context: Context, val fragment: GarageFragment): RecyclerView.Adapter<GarageAdapter.GarageViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var vehicles_list = emptyList<OutputVehicle>()

    inner class GarageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val brandViewHolder: TextView = itemView.findViewById(R.id.brand_txt)
        val license_plateViewHolder: TextView = itemView.findViewById(R.id.license_plate_txt)
        val imageViewHolder: ImageView = itemView.findViewById(R.id.vehicle_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarageViewHolder {
        // Criar nova view que define a User Interface do item da lista
        val view = inflater.inflate(R.layout.recycler_truck_line, parent, false)
        return GarageViewHolder(view)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onBindViewHolder(holder: GarageViewHolder, position: Int) {
        val currentTruck = vehicles_list[position]

        val base64String: String = currentTruck.image
        val img : ByteArray? = Base64.decode(base64String, Base64.DEFAULT)

        val bm = BitmapFactory.decodeByteArray(img, 0, img!!.size)

        holder.brandViewHolder.text = holder.itemView.context.getString(R.string.vehicle_brand, currentTruck.brand)
        holder.license_plateViewHolder.text = holder.itemView.context.getString(R.string.vehicle_license, currentTruck.license_plate)
        holder.imageViewHolder.setImageBitmap(bm)
        holder.itemView.setOnClickListener {
            Log.d("aa", "esta aqui um camiao")
        }
    }

    fun setVehicles(vehicles: List<OutputVehicle>){
        Log.d("camioes", vehicles.toString())
        this.vehicles_list = vehicles
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return vehicles_list.size
    }
}

