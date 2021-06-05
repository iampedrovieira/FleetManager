package com.example.fleetmanager.uiManagement.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fleetmanager.R
import com.example.fleetmanager.api.Endpoints
import com.example.fleetmanager.api.OutputManagement
import com.example.fleetmanager.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        toolbar = root.findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.title_profile)

        getUserInfo()
        return root
    }

    private fun getUserInfo() {
        val sharedPref: SharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val company_key = sharedPref.getString(getString(R.string.company), "aaaa")
        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getManagementInfo(company_key)
        call.enqueue(object : Callback<OutputManagement> {
            override fun onResponse( call: Call<OutputManagement>, response: Response<OutputManagement> ) {
                if (response.isSuccessful) {
                    Log.d("****ProfilePage", response.body().toString())
                }
            }

            override fun onFailure(call: Call<OutputManagement>, t: Throwable) {
                Log.d("****ProfilePage", t.toString())
                Log.d("****ProfilePage", call.toString())
            }
        })
    }
}