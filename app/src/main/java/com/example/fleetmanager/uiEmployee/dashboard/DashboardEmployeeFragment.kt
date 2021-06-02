package com.example.fleetmanager.uiEmployee.dashboard

import android.app.ActionBar
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.fleetmanager.MainActivity
import com.example.fleetmanager.MainActivityEmployee
import com.example.fleetmanager.R
import com.example.fleetmanager.dto.GoogleMapDTO
import com.example.fleetmanager.dto.PolyLine
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

const val LOCATION_PERMISSION_REQUEST_CODE = 1000

class DashboardEmployeeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var gMap : GoogleMap
    private lateinit var teste:Button
    private lateinit var lastLocation : Location
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback : LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private lateinit var destination_input: EditText
    var once: Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard_employee, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_dashboard)
        toolbar.inflateMenu(R.menu.dashboard_menu)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }

        destination_input = root.findViewById(R.id.destination_input)

        teste = root.findViewById(R.id.teste)
        teste.setOnClickListener {
            val url = getDirectionUrl(lastLocation.latitude, lastLocation.longitude, destination_input.text.toString())
            GetDirection(url).execute()
        }

        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                lastLocation = p0!!.lastLocation
                var location = LatLng(lastLocation.latitude, lastLocation.longitude)
                if(!once){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))
                    once = true
                }
            }
        }
        createLocationRequest()



        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu);

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.chat_nav -> {
                (activity as MainActivityEmployee?)!!.replaceFragmentChat()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        val currentLatLng = LatLng(41.15822462366187, -8.62920595465106)
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
        if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        gMap.isMyLocationEnabled = true
    }

    private fun getDirectionUrl(origin_lat: Double, origin_long: Double, address : String) : String{
        //origin of route
        val str_origin = "origin=" + origin_lat + "," + origin_long

        //origin of route
        val str_destination = "destination=" + address.split(" ").joinToString("+")

        val mode = "mode=driving"

        val parameters = "$str_origin&$str_destination&$mode"

        //Output format
        val output = "json"

        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyAUWK2jFqvY1X2QyHAJLLR9qI1r7hIPIK4"
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    inner class GetDirection(val url: String) : AsyncTask<Void,Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            val result = ArrayList<List<LatLng>>()
            try {
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)

                val path = ArrayList<LatLng>()

                for(i in 0..(respObj.routes[0].legs[0].steps.size-1)){
                    /**val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble(),
                    respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())

                    path.add(startLatLng)

                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble(),
                    respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())*/

                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }

                result.add(path)
            }catch (e: Exception){
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>?) {
            val lineoption = PolylineOptions()

            for (i in result!!.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }

            gMap.addPolyline(lineoption)
        }

    }

    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}