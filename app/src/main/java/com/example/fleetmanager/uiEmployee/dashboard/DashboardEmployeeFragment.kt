package com.example.fleetmanager.uiEmployee.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.fleetmanager.MainActivityEmployee
import com.example.fleetmanager.R
import com.example.fleetmanager.dto.GoogleMapDTO
import com.example.fleetmanager.realtimeLocation.*
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

const val LOCATION_PERMISSION_REQUEST_CODE = 1000

class DashboardEmployeeFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2200
    }

    private lateinit var locationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var locationFlag = true
    private var driverOnlineFlag = false
    private var currentPositionMarker: Marker? = null
    private val googleMapHelper = GoogleMapHelper()
    private lateinit var firebaseHelper : FirebaseHelper
    private val markerAnimationHelper = MarkerAnimationHelper()
    private val uiHelper = UiHelper()
    private var key: String? = null

    private lateinit var gMap : GoogleMap
    private lateinit var lastLocation : Location
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    var once: Boolean = false

    private var active : Boolean = false

    private lateinit var sharedPref : SharedPreferences

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

        val mapFragment =  childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        key = sharedPref.getString(getString(R.string.uid),"0000")
        firebaseHelper = FirebaseHelper(key!!)

        driverOnlineFlag = sharedPref.getBoolean("active", false)

        val driverStatusTextView = root.findViewById<TextView>(R.id.driverStatusTextViewX)

        if(driverOnlineFlag){
            driverStatusTextView.text = resources.getString(R.string.online_driver)
        }else{
            driverStatusTextView.text = resources.getString(R.string.offline)
            firebaseHelper.deleteDriver()
        }



        mapFragment.getMapAsync { gMap = it }
        createLocationCallback()
        locationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = uiHelper.getLocationRequest()
        if (!uiHelper.isPlayServicesAvailable(requireContext())) {
            Toast.makeText(context, "Play Services did not installed!", Toast.LENGTH_SHORT).show()
        } else requestLocationUpdate()




        return root
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult!!.lastLocation == null) return
                val latLng = LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)
                Log.e("Location", latLng.latitude.toString() + " , " + latLng.longitude)
                if (locationFlag) {
                    locationFlag = false
                    animateCamera(latLng)
                }
                    if (sharedPref.getBoolean("active", false)) firebaseHelper.updateDriver(Driver(lat = latLng.latitude, lng = latLng.longitude, driverId = key.toString()))
                    showOrAnimateMarker(latLng)

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(requireContext())) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                DashboardEmployeeFragment.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        if (uiHelper.isLocationProviderEnabled(requireContext()))
            uiHelper.showPositiveDialogWithListener(requireContext(), resources.getString(R.string.need_location), resources.getString(R.string.location_content), object : IPositiveNegativeListener {
                override fun onPositive() {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }, "Turn On", false)
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun showOrAnimateMarker(latLng: LatLng) {
        if (currentPositionMarker == null)
            currentPositionMarker = gMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng))
        else markerAnimationHelper.animateMarkerToGB(currentPositionMarker!!, latLng, LatLngInterpolator.Spherical())
    }
    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        gMap.animateCamera(cameraUpdate, 10, null)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == DashboardEmployeeFragment.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            val value = grantResults[0]
            if (value == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(context, "Location Permission denied", Toast.LENGTH_SHORT).show()
            } else if (value == PackageManager.PERMISSION_GRANTED) requestLocationUpdate()
        }
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

        if(arguments != null ){
            val destination = requireArguments().getString("DESTINATION")
            val ori_lat = requireArguments().getDouble("ORIGIN_LAT")
            val ori_lng = requireArguments().getDouble("ORIGIN_LNG")
            val url = getDirectionUrl(ori_lat,ori_lng, destination!!)
            GetDirection(url).execute()
        }
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

}