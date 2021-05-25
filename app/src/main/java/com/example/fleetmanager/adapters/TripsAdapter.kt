package com.example.fleetmanager.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.fleetmanager.R
import com.example.fleetmanager.api.OutputTrip
import com.example.fleetmanager.api.OutputVehicle
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TripsAdapter(val context: Context, val fragment: Fragment): RecyclerView.Adapter<TripsAdapter.TripsViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trips_list = emptyList<OutputTrip>()

    inner class TripsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val trip_dateViewHolder: TextView = itemView.findViewById(R.id.trip_date)
        val trip_distanceViewHolder: TextView = itemView.findViewById(R.id.trip_distance)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TripsAdapter.TripsViewHolder {
        val view = inflater.inflate(R.layout.recycler_trip_line, parent, false)
        return TripsViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: TripsAdapter.TripsViewHolder, position: Int) {
        val currentTrip = trips_list[position]

        //val trip_local_date = convertToLocalDateViaInstant(currentTrip.date)
        //val formatter = DateTimeFormatter.ofPattern("dd-MM-yyy")
        //val trip_date_formatted = trip_local_date.format(formatter)
        //val formatter_string = DateTimeFormatter.ISO_DATE
        //val trip_date_string = LocalDate.parse(trip_date_formatted, formatter_string)

        //val trip_localDateTime = LocalDateTime.parse(currentTrip.date.toString())
        val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val outputDate = formatter.format(parser.parse(currentTrip.date.toString()))


        //val trip_date = convertToDateViaInstant(trip_date_string)

        holder.trip_dateViewHolder.text = holder.itemView.context.getString(R.string.trip_date, outputDate)
        holder.trip_distanceViewHolder.text = holder.itemView.context.getString(R.string.trip_distance, currentTrip.travel_distance.toString())
    }

    fun convertToDateViaInstant(dateToConvert: LocalDate): Date{
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
    }

    fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate{
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun setTrips(trips: List<OutputTrip>){
        Log.d("camioes", trips.toString())
        this.trips_list = trips
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return trips_list.size
    }
}